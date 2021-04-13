package it.wego.persistence.objects;

import it.wego.persistence.objects.AbstractOperations.AbstractDateOperation;
import it.wego.persistence.objects.AbstractOperations.AbstractFieldOperation;
import it.wego.persistence.objects.AbstractOperations.AbstractIntOperation;
import it.wego.persistence.objects.AbstractOperations.AbstractObjectOperation;
import it.wego.persistence.objects.AbstractOperations.AbstractStringOperation;
import java.util.Date;

/**
 *
 * @author aleph
 */
public class SimpleAssignments {

	public static class DateAssignment extends AbstractDateOperation implements Assignment {

		public DateAssignment(String field, Date value) {
			super(field, value);
		}

		@Override
		public String toString() {
			return getField() + " = :" + getParameterName();
		}
	}
        
	public static class StringAssignment extends AbstractStringOperation implements Assignment {

		public StringAssignment(String field, String value) {
			super(field, value);
		}

		@Override
		public String toString() {
			return getField() + " = :" + getParameterName();
		}
	}

	public static class IntAssignment extends AbstractIntOperation implements Assignment {

		public IntAssignment(String field, int value) {
			super(field, value);
		}

		@Override
		public String toString() {
			return getField() + " = :" + getParameterName();
		}
	}

	public static class ObjectAssignment extends AbstractObjectOperation implements Assignment {

		public ObjectAssignment(String field, Object value) {
			super(field, value);
		}

		@Override
		public String toString() {
			return getField() + " = :" + getParameterName();
		}
	}

	public static class NullAssignment extends AbstractFieldOperation implements Assignment {

		public NullAssignment(String field) {
			super(field);
		}

		@Override
		public String toString() {
			return getField() + " = NULL";
		}
	}
}
