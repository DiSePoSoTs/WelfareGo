Ext.define('Ext.ux.VirtualKeyboardButton', {
    extend:'Ext.button.Button',
    text:'tastiera virtuale',
    iconCls:'ux-virtualkeyboard-icon',
    alias: 'widget.vkbutton',
    handler:function(){
        if(!this.virtualKeyboard){
            this.initVirtualKeyboard();
        }
        this.virtualKeyboardWindow.show();
        this.virtualKeyboardWindow.getEl().alignTo(this.getKeyboardTarget());
    },
    initVirtualKeyboard:function(){
        var anElement,me=this;
        Ext.select('input[type="text"], textarea').each(function(element){
            anElement=element;
            element.addListener('focus',function(event,element){
                me.setKeyboardTarget(Ext.get(element));
            });
        });
        this.virtualKeyboardWindow=Ext.create('Ext.window.Window',{
            title:'tastiera virtuale',
            layout:'fit',
            width:400,
            height:170,
            resizable:false,
            items:this.virtualKeyboard=Ext.create('Ext.ux.VirtualKeyboard',{
                keyboardTarget:this.getKeyboardTarget()||anElement,
                languageSelection:true,
                language:'Italian'
            }),
            closeAction:'hide',
            listeners:{
                hide:{
                    fn:function(){
                        delete this.virtualKeyboardWindow;
                        delete this.virtualKeyboard;
                    },
                    scope:this
                }
            }
        });
    },
    setKeyboardTarget:function(keyboardTarget){
        if(this.keyboardTarget===keyboardTarget){
            return;
        }
        this.keyboardTarget=keyboardTarget;
        if(this.virtualKeyboard){
            this.virtualKeyboard.setKeyboardTarget.call(this.virtualKeyboard,keyboardTarget);
        }
    },
    getKeyboardTarget:function(){
        return this.keyboardTarget;
    },
    listeners:{		
        afterrender:function(){
            var me=this,onFocus=function(event,element){
                me.setKeyboardTarget(Ext.get(element||event.srcElement));
            };
            var anElement;
            Ext.select('input[type="text"], textarea').each(function(element){
                anElement=Ext.get(element);
                anElement.addListener('focus',onFocus);
            });
            this.setKeyboardTarget(anElement);
        }
    }
});