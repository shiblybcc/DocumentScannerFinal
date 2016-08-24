package de.rwthaachen.cbmb.Utility;


import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

public class ApplicationHelpers {

    public static void setErrorMessage(String errorMessage, String componentId) {
        FacesContext fc = FacesContext.getCurrentInstance();
        FacesMessage msg = new FacesMessage(errorMessage);
        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        fc.addMessage(componentId, msg);
    }

    public static void setSuccessMessage(String successMessage, String componentId) {
        FacesContext fc = FacesContext.getCurrentInstance();
        FacesMessage msg = new FacesMessage(successMessage);
//        msg.setSeverity(FacesMessage.SEVE);
        fc.addMessage(componentId, msg);
    }

    public static String getFilename(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
    }


}
