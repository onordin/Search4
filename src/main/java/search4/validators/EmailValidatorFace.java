package search4.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value="emailValidator")
public class EmailValidatorFace implements Validator {

    public void validate(FacesContext facesContext, UIComponent uiComponent, Object value) throws ValidatorException {
        HtmlInputText htmlInputText = (HtmlInputText) uiComponent;

        EmailValidator emailValidator = new EmailValidator();
        if (!emailValidator.validateEmail(value)) {
            FacesMessage facesMessage = new FacesMessage(htmlInputText.getLabel() + ": email format is not valid");
            throw new ValidatorException(facesMessage); //TODO figure out how to send this to and properly use in frontend?
        }
    }
}
