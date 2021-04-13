package it.wego.welfarego.users;

/**
 *
 * @author giuseppe
 */
public enum UserType {

    ACC_UOT {
        @Override
        public ConnectedUserInformations getUtente() {
            return new OperatoreAccessoUOT();
        }
    },
    OPE_UOT {
        @Override
        public ConnectedUserInformations getUtente() {
            return new OperatoreUOT();
        }
    },
    AS_UOT{
        @Override
        public ConnectedUserInformations getUtente() {
            return new AssistenteSocialeUOT();
        }
    },
    CO_UOT{
        @Override
        public ConnectedUserInformations getUtente() {
            return new CoordinatoreUOT();
        }
    },
    OPE_CENTR{
        @Override
        public ConnectedUserInformations getUtente() {
            return new OperatoreSedeCentrale();
        }
    },
    PO_CENTR{
        @Override
        public ConnectedUserInformations getUtente() {
            return new ResponsabilePosizioneOrganizzativa();
        }
    },
    DIR_CENTR{
        @Override
        public ConnectedUserInformations getUtente() {
            return new DirettoreSedeCentrale();
        }
    },
    DIR_AREA{
        @Override
        public ConnectedUserInformations getUtente() {
            return new DirettoreArea();
        }
    },
    EDU_UOT{
        @Override
        public ConnectedUserInformations getUtente() {
            return new EduUOT();
        }
    },
    ADMIN {
        @Override
        public ConnectedUserInformations getUtente() {
            return new Amministratore();
        }
    };

    public abstract ConnectedUserInformations getUtente();
}
