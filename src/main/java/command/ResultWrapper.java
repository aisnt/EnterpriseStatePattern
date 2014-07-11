package command;

/**
 * Created by david.hislop@korwe.com on 2014/06/21.
 */
public class ResultWrapper<T> {
    T t;
    public ResultWrapper(T t){
        this.t = t;
    }
    public  T get() {
        return t;
    }

    @Override
    public String toString() {
        String result = t.toString();
        return result;
    }
}
