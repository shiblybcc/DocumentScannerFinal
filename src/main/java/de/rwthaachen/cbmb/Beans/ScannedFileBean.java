package de.rwthaachen.cbmb.Beans;

import de.rwthaachen.cbmb.Domain.ScannedFile;
import de.rwthaachen.cbmb.Domain.User;
import de.rwthaachen.cbmb.Service.ScannedFileService;
import de.rwthaachen.cbmb.Utility.ApplicationHelpers;
import de.rwthaachen.cbmb.Utility.FileEncryption;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.activation.MimetypesFileTypeMap;
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
        FacesContext fc = FacesContext.getCurrentInstance();
        InputStream inputStream = file.getInputStream();
        String filename = ApplicationHelpers.getFilename(file);
        FileEncryption fileEncryption = new FileEncryption();
        byte[] bytes = fileEncryption.encryptImage(inputStream);

        scannedFile.setName(filename);
        scannedFile.setKey(fileEncryption.encryptKey());
        scannedFile.setScannedImage(bytes);

        try{
            scannedFileService.save(scannedFile);
            ApplicationHelpers.setSuccessMessage("File uploaded successfully!", null);
            fc.renderResponse();
        }
        catch (Exception e ){
            ApplicationHelpers.setErrorMessage("There was an error while saving the file, please try again", null);
            fc.renderResponse();
        }


        inputStream.close();
        return "index.xhtml";
    }


    public void download()  throws IOException, GeneralSecurityException, DecoderException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        ec.responseReset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
        OutputStream output = ec.getResponseOutputStream();

        InputStream privateKey1 = file.getInputStream();
        InputStream privateKey2 = file2.getInputStream();

        String scannedFileId = fc.getExternalContext().getRequestParameterMap().get("uploadKey:scannedFileId");
        scannedFile = scannedFileService.findById(Integer.parseInt(scannedFileId));

        // The Save As popup magic is done here. You can give it any file name you want, this only won't work in MSIE, it will use current request URL as file name instead.
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + scannedFile.getId() +
                "-" + scannedFile.getName() + "\"");

        try{
            InputStream is = new ByteArrayInputStream(scannedFile.getScannedImage());
            FileEncryption fileEncryption = new FileEncryption();
            fileEncryption.decryptKey(scannedFile.getKey(), privateKey1, privateKey2);
            CipherInputStream cis = fileEncryption.decryptImage(is);
            ApplicationHelpers.setSuccessMessage("Scanned files successfully decrypted and downloaded", null);
            IOUtils.copy(cis, output);
        } catch (Exception e){
            ApplicationHelpers.setErrorMessage("There was an error. Please try again!", null);
        }
        fc.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
    }

    public void findFileById() throws GeneralSecurityException, DecoderException, IOException {
        ScannedFile found = scannedFileService.findById(this.scannedFile.getId());
    }

    public List<ScannedFile> findAllScannedFiles() {
        scannedFiles = scannedFileService.findAll();
        return scannedFiles;
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
