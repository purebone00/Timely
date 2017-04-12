package validators;

import java.math.BigDecimal; 
import javax.faces.component.UIComponent;  
import javax.faces.context.FacesContext;  
import javax.faces.convert.NumberConverter;
import javax.faces.convert.FacesConverter;

/**
 * Converts a Double or Long value provided by standard jsf number
 * converter to a BigDecimal value
 *
 * To get a locale-sensitive converter, java.text.NumberFormat is used
 * (through javax.faces.convert.NumberConverter).
 * The parsing done by java.math.BigDecimal is not affected by locale.
 * See javax.faces.convert.BigDecimalConverter
 *
 */  
@FacesConverter("bigDecimalConverter")
public class BigDecimalConverter extends NumberConverter {  
	 
	/*
	 * don't use BigDecimal ctor
	 * see java.math.BigDecimal
	 */  
	public Object getAsObject(FacesContext context, UIComponent component, String string) {  
		Object value = super.getAsObject(context, component, string);  
		if (value instanceof Long) 
			return BigDecimal.valueOf((Long) value);  
		if (value instanceof Double) 
			return BigDecimal.valueOf((Double) value);  
		return value;  
	}  
}