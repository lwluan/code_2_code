package javax.validation.constraints;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 手机号码验证
 * 
 * @author leiwuluan
 *
 */
public class MobileValidator implements ConstraintValidator<Mobile, String> {

	@Override
	public void initialize(Mobile constraintAnnotation) {

	}

	@Override
	public boolean isValid(String phone, ConstraintValidatorContext context) {
		String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
		if (phone.length() != 11) {

			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("{mobile.null}").addConstraintViolation();
			
			return false;
		} else {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(phone);
			boolean isMatch = m.matches();
			if (isMatch) {
				return true;
			} else {
				return false;
			}
		}
	}

}
