package impl3a;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EPPStage {

    final String name;
    final String kind;

    final List<EPPStage> stages;

    public EPPStage(String name, String kind, EPPStage[] stages) {
        this.name = name;
        this.kind = kind;
        this.stages = Arrays.asList(stages);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        result.append( this.getClass().getName() );
        result.append( " Object {" );
        result.append(newLine);

        //determine fields declared in this class only (no fields of superclass)
        Field[] fields = this.getClass().getDeclaredFields();

        //print field names paired with their values
        for ( Field field : fields  ) {
            result.append("  ");
            try {
                result.append( field.getName() );
                result.append(": ");
                //requires access to private field:
                result.append( field.get(this) );
            } catch ( IllegalAccessException ex ) {
                System.out.println(ex);
            }
            result.append(newLine);
        }
        result.append("}");

        return result.toString();
    }
}
