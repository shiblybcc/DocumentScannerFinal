package de.rwthaachen.cbmb.Beans;

import de.rwthaachen.cbmb.Domain.ScannedFile;
import de.rwthaachen.cbmb.Domain.User;
import de.rwthaachen.cbmb.Service.ScannedFileService;
import de.rwthaachen.cbmb.Utility.FileEncryption;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.io.IOUtils;

import javax.crypto.CipherInputStream;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.imageio.ImageIO;
import javax.servlet.http.Part;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class ScannedFileBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManagedProperty("#{scannedFileService}")
    ScannedFileService scannedFileService;

    private ScannedFile scannedFile = new ScannedFile();

    private List<ScannedFile> scannedFiles;

    private Part file;
    private Part file2;

    public String upload() throws IOException, GeneralSecurityException, DecoderException {
        InputStream inputStream = file.getInputStream();
        String filename = getFilename(file);
        FileEncryption fileEncryption = new FileEncryption();
        byte[] bytes = fileEncryption.encryptImage(inputStream);

        scannedFile.setName(filename);
        scannedFile.setKey(fileEncryption.encryptKey());
        scannedFile.setScannedImage(bytes);
        scannedFileService.save(scannedFile);

        inputStream.close();
        return "index.xhtml";
    }

    public void validateForm (ComponentSystemEvent event) throws IOException{
        String alphaNumeric = "^[a-zA-Z0-9]*$";
        FacesContext fc = FacesContext.getCurrentInstance();
        UIComponent components = event.getComponent();
        UIInput uiInputImage = (UIInput) components
                .findComponent("scannedImage");
        Part file;
        file = (Part)uiInputImage.getLocalValue();
        String scannedImageId = uiInputImage.getClientId();


        UIInput uiInputfallnummer = (UIInput) components
                .findComponent("fallnummer");
        String fallnummer = uiInputfallnummer.getLocalValue() == null ? ""
                : uiInputfallnummer.getLocalValue().toString();
        String fallnummerId = uiInputImage.getClientId();

        //validate fallnummer
        if (!(fallnummer.matches(alphaNumeric))){
            FacesMessage msg = new FacesMessage(
                    "Only numers and letters are allowed");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            fc.addMessage(fallnummerId, msg);
            fc.renderResponse();
        }

        //validate uploaded image
        try (InputStream input = file.getInputStream()) {

            int maxFileSize = 2*1024*1024;
            if (!(file.getSize()<maxFileSize)){
                FacesMessage msg = new FacesMessage(
                        "File size must be less than 2Mb");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                fc.addMessage(scannedImageId, msg);
                fc.renderResponse();
            }

            try {
                ImageIO.read(input).toString();
            }
            catch (Exception e) {
                FacesMessage msg = new FacesMessage(
                        "Only BMP, GIF, JPG or PNG type images are allowed");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                fc.addMessage(scannedImageId, msg);
                fc.renderResponse();
            }
        }
    }

    public void download() throws IOException, GeneralSecurityException, DecoderException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        ec.responseReset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
//        ec.setResponseContentType(contentType); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
//        ec.setResponseContentLength(contentLength); // Set it with the file size. This header is optional. It will work if it's omitted, but the download progress will be unknown.
        OutputStream output = ec.getResponseOutputStream();
        // Now you can write the InputStream of the file to the above OutputStream the usual way.
        InputStream privateKey1 = file.getInputStream();
        InputStream privateKey2 = file2.getInputStream();


        String scannedFileId = fc.getExternalContext().getRequestParameterMap().get("uploadKey:scannedFileId");
        scannedFile = scannedFileService.findById(Integer.parseInt(scannedFileId));

        // The Save As popup magic is done here. You can give it any file name you want, this only won't work in MSIE, it will use current request URL as file name instead.
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + scannedFile.getId() +
                "-" + scannedFile.getName() + "\"");

        InputStream is = new ByteArrayInputStream(scannedFile.getScannedImage());
        FileEncryption fileEncryption = new FileEncryption();
        fileEncryption.decryptKey(scannedFile.getKey(), privateKey1, privateKey2);
        CipherInputStream cis = fileEncryption.decryptImage(is);

        FacesMessage msg = new FacesMessage("Scanned files successfully decrypted and downloaded");
        fc.addMessage(null, msg);

        IOUtils.copy(cis, output);
        fc.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
    }


    public void findFileById() throws GeneralSecurityException, DecoderException, IOException {
        ScannedFile found = scannedFileService.findById(this.scannedFile.getId());
//        this.scannedFile.setId(found.getId());
//        this.scannedFile.setFallnummer(found.getFallnummer());
//        this.scannedFile.setScannedImage(found.getScannedImage());
    }

    public List<ScannedFile> findAllScannedFiles() {
        scannedFiles = new ArrayList<>();
        scannedFileService.findAll();
        return scannedFiles;
    }


    private static String getFilename(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
    }

    public ScannedFileService getScannedFileService() {
        return scannedFileService;
    }

    public void setScannedFileService(ScannedFileService scannedFileService) {
        this.scannedFileService = scannedFileService;
    }

    public ScannedFile getScannedFile() {
        return scannedFile;
    }

    public void setScannedFile(ScannedFile scannedFile) {
        this.scannedFile = scannedFile;
    }

    public List<ScannedFile> getScannedFiles() {
        return scannedFiles;
    }

    public void setScannedFiles(List<ScannedFile> scannedFiles) {
        this.scannedFiles = scannedFiles;
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public Part getFile2() {
        return file2;
    }

    public void setFile2(Part file2) {
        this.file2 = file2;
    }
}
