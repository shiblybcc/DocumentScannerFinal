package de.rwthaachen.cbmb.Service;

import de.rwthaachen.cbmb.Domain.ScannedFile;

import java.util.List;

public interface ScannedFileService {

    public List<ScannedFile> findAll();

    public ScannedFile findById(Integer id);

    public ScannedFile save(ScannedFile scannedFile);

    public void destroy(ScannedFile scannedFile);
}
