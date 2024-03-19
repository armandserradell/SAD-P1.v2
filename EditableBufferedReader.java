import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class EditableBufferedReader extends BufferedReader {

    

    EditableBufferedReader(InputStreamReader in){
        super(in);
    }

    public static void setRaw() throws IOException {
        
        String [] modeRaw = {"/bin/sh", "-c", "stty -echo raw </dev/tty"};
        try {
            Runtime.getRuntime().exec(modeRaw).waitFor();  
        }catch (Exception e) {
            System.out.println("Error");
        } 
    }
    
    public static void unsetRaw() throws IOException {
        
        String[] modeCooked = {"/bin/sh", "-c", "stty echo cooked </dev/tty"};
        try{
            Runtime.getRuntime().exec(modeCooked).waitFor();
        } catch (IOException | InterruptedException e){
           System.out.println("Error");
        }
    }

    public int read() throws IOException {
        int entrada = 0;

        
        entrada = super.read();
        if(entrada == Dictionary.ESC){
            entrada = super.read();
            if(entrada == Dictionary.CORCHETE){
                entrada = super.read();
                switch(entrada){
                    case Dictionary.DERECHA:
                        return Dictionary.FLECHA_DERECHA;
                    case Dictionary.IZQUIERDA:
                        return Dictionary.FLECHA_IZQUIERDA;
                    case Dictionary.HOME:
                        return Dictionary.TECLA_HOME;
                    case Dictionary.END:
                        return Dictionary.TECLA_FIN;
                    case Dictionary.INSERT:
                        if(super.read() == Dictionary.TILDE){
                            return Dictionary.TECLA_INSERT;
                        }
                        return -1;
                    case Dictionary.DELETE:
                        if(super.read() == Dictionary.TILDE){
                            return Dictionary.TECLA_DELETE;
                        }
                        return -1;
                    default:
                        return -1;
                }
            }
        } else {
            return entrada;
        }
        return -1;
    }

    public String readLine() throws IOException {
        int entrada = 0;
        int aux;
        Line linea = new Line();

        this.setRaw();

        while((entrada = this.read()) != Dictionary.ENTER){
            switch(entrada){
                case Dictionary.FLECHA_DERECHA:
                    if(linea.mover_derecha()){
                        System.out.print("\033[C");
                    } else {
                        System.out.print('\007');
                    }
                break;
                case Dictionary.FLECHA_IZQUIERDA:
                    if(linea.mover_esquerra()){
                        System.out.print("\033[D");
                    } else {
                        System.out.print('\007');
                    }
                break;
                case Dictionary.TECLA_HOME:
                    aux = linea.home();
                    System.out.print("\033[" + aux + "D");
                break;
                case Dictionary.TECLA_FIN:
                    aux = linea.end();
                    System.out.print("\033[" + aux + "C");
                break;
                case Dictionary.TECLA_DELETE:
                    if(linea.delete()){
                        System.out.print("\033[P");
                    } else {
                        System.out.print('\007');
                    }
                break;
                case Dictionary.TECLA_INSERT:
                    linea.insertarMode();
                    System.out.print("\033[4l");
                break;
                case Dictionary.BACKSPACE:
                    if(linea.backspace()){
                        System.out.print("\033[D");
                        System.out.print("\033[P");
                    } else {
                        System.out.print('\007');
                    }
                break;
                default:
                    boolean insert = linea.insertar_caracter((char) entrada);
                    if(insert){
                        System.out.print("\033[4h");
                    }
                    System.out.print((char) entrada);
                break;
            }
        }
        this.unsetRaw();

        return linea.toString();
    }
}
