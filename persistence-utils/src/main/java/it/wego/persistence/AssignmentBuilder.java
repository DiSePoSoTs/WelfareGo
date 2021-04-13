package it.wego.persistence;

import it.wego.persistence.objects.Assignment;
import it.wego.persistence.objects.SimpleAssignments.DateAssignment;
import it.wego.persistence.objects.SimpleAssignments.IntAssignment;
import it.wego.persistence.objects.SimpleAssignments.NullAssignment;
import it.wego.persistence.objects.SimpleAssignments.ObjectAssignment;
import it.wego.persistence.objects.SimpleAssignments.StringAssignment;
import java.util.Date;

/**
 *
 * @author aleph
 */
public class AssignmentBuilder {

    public static Assignment assign(String field, Date value) {
        return new DateAssignment(field, value);
    }

    public static Assignment assign(String field, String value) {
        return new StringAssignment(field, value);
    }

    public static Assignment assign(String field, int value) {
        return new IntAssignment(field, value);
    }

    public static Assignment assign(String field, Object value) {
        return new ObjectAssignment(field, value);
    }

    public static Assignment assignNull(String field) {
        return new NullAssignment(field);
    }
}
