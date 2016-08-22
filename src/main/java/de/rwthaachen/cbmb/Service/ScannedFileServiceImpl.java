package de.rwthaachen.cbmb.Service;


import com.google.common.collect.Lists;
import de.rwthaachen.cbmb.Domain.ScannedFile;
import de.rwthaachen.cbmb.Repository.ScannedFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("scannedFileService")
@Repository
@Transactional
public class ScannedFileServiceImpl implements ScannedFileService {

    @Autowired
    private ScannedFileRepository scannedFileRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ScannedFile> findAll() {
        return Lists.newArrayList(scannedFileRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public ScannedFile findById(Integer id) {
        return scannedFileRepository.findOne(id);
    }


    @Override
    @Transactional
    public ScannedFile save(ScannedFile scannedFile) {
        return scannedFileRepository.save(scannedFile);
    }

    @Override
    public void destroy(ScannedFile scannedFile) {
        scannedFileRepository.delete(scannedFile);
    }

}
