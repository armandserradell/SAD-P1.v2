import java.util.ArrayList;

public class Line {
    
    private int cursor;
    private boolean insertar;
    private ArrayList<Character> buffer;

    public Line(){
        this.cursor = 0;
        this.insertar = true;
        this.buffer = new ArrayList<Character>();
    }

    public int getPos(){
        return this.cursor;
    }

    public void setPos(int posinci){
        this.cursor = posinci;
    }

    public int home(){
        int aux = cursor;
        this.cursor = 0;
        return aux;
    }

    public int end(){
        int aux = cursor;
        this.cursor = this.buffer.size();
        return (this.buffer.size() - aux);
    }

    public boolean mover_derecha(){
        if(this.cursor < this.buffer.size()){
            this.cursor ++;
            return true;
        } else {
            return false;
        }
    }

    public boolean mover_esquerra(){
        if(this.cursor > 0){
            this.cursor--;
            return true;
        } else {
            return false;
        }
    }

    public boolean backspace(){
        if(!buffer.isEmpty()){
            this.buffer.remove(this.cursor - 1);
            this.mover_esquerra();
            return true;
        } else {
            return false;
        }
    }

    public boolean delete(){
        if(this.cursor < this.buffer.size() && !buffer.isEmpty()){
            this.buffer.remove(this.cursor);
            return true;
        } else {
            return false;
        }
    }

    public void insertarMode(){
        insertar = !insertar;
    }

    public boolean insertar_caracter(char c){
        if(insertar){
            this.buffer.add(cursor, c);
        } else {
            if(cursor < this.buffer.size()){
                this.buffer.set(cursor, c);
            } else {
                this.buffer.add(cursor, c);
            }
        }
        this.mover_derecha();
        return insertar;
    }

    public String toString(){
        String str = "";
        for(Character c: buffer){
            str = str + c;
        }
        return str;
    }
}
