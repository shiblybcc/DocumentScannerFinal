package de.rwthaachen.cbmb.Beans;

import de.rwthaachen.cbmb.Domain.ScannedFile;
import de.rwthaachen.cbmb.Service.ScannedFileService;
import de.rwthaachen.cbmb.Utility.FileEncryption;
import org.apache.commons.codec.DecoderException;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.List;

@ManagedBean
@ApplicationScoped
public class ImageViewerBean {

    @ManagedProperty("#{scannedFileService}")
    ScannedFileService scannedFileService;

    private ScannedFile scannedFile = new ScannedFile();

    private List<ScannedFile> scannedFiles;


    public StreamedContent getImage() throws IOException, DecoderException, GeneralSecurityException {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return new DefaultStreamedContent();
        }
        else {
            String scannedFileId = context.getExternalContext().getRequestParameterMap().get("scannedFileId");
            scannedFile = scannedFileService.findById(Integer.parseInt(scannedFileId));

            InputStream is = new ByteArrayInputStream(scannedFile.getScannedImage());
//            InputStream is = new ByteArrayInputStream(scannedFile.getScannedImage());
            FileEncryption fileEncryption = new FileEncryption();
//            fileEncryption.decryptKey(scannedFile.getKey());


            return new DefaultStreamedContent(fileEncryption.decryptImage(is));
        }
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
}
