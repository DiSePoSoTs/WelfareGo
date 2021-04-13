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
//                log('vk focus event ',event,' on ',element);
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
//                        log('closing vk');
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
//        log('keyboard target : '+keyboardTarget);
        this.keyboardTarget=keyboardTarget;
        if(this.virtualKeyboard){
            this.virtualKeyboard.setKeyboardTarget.call(this.virtualKeyboard,keyboardTarget);
        }
    },
    getKeyboardTarget:function(){
        return this.keyboardTarget;
    //		return document.activeElement;
    },
    listeners:{		
        afterrender:function(){
            var me=this,onFocus=function(event,element){
//                log('vk focus event ',event,' on ',element);
                me.setKeyboardTarget(Ext.get(element||event.srcElement));
            };
//            log('initializing vk button');
//            document.addEventListener('DOMNodeInserted',function(event){
//                log('new dom mod ',event);
//                var element=event.srcElement;
//                log('new dom mod element ',element);    
//                if(element.type=="textarea" || element.type=="text"){
//                    log('attaching listener to element',element);    
//                    element.addEventListener('focus',onFocus);
//                }
//            });
            var anElement;
            Ext.select('input[type="text"], textarea').each(function(element){
                anElement=element;
                element.addListener('focus',onFocus);
            });
            this.setKeyboardTarget(Ext.get(anElement));
        }
    }
});