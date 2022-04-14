package io.kyligence.notebook.console.service;

import io.kyligence.notebook.console.NotebookConfig;
import io.kyligence.notebook.console.bean.entity.UserAction;
import io.kyligence.notebook.console.bean.model.UploadedFiles;
import io.kyligence.notebook.console.dao.UserActionRepository;
import io.kyligence.notebook.console.util.JacksonUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserActionRepository userActionRepository;

    private static NotebookConfig config = NotebookConfig.getInstance();



    public UserAction getUserAction(String user) {
        List<UserAction> userActions = userActionRepository.findByUser(user);

        if (userActions == null || userActions.isEmpty()) {
            return null;
        }
        return userActions.get(0);
    }

    public void saveOpenedExecfiles(String user, String openedNotebooks) {
        UserAction userAction = getUserAction(user);

        if (userAction == null) {
            userAction = new UserAction();
        }

        userAction.setUser(user);
        userAction.setOpenedNotebooks(openedNotebooks);
        userActionRepository.save(userAction);
    }

    public UploadedFiles getUploadedFileRecords(String user){
        UserAction userAction = getUserAction(user);
        return userAction == null || userAction.getUploadedFiles() == null
                ? null : JacksonUtils.readJson(userAction.getUploadedFiles(), UploadedFiles.class);
    }

    @Transactional
    public void saveUploadedFiles(String user, String fileName, Double fileSize) {
        UserAction userAction = getUserAction(user);

        if (userAction == null) {
            userAction = new UserAction();
        }

        UploadedFiles existFiles = userAction.getUploadedFiles() == null ?
                null : JacksonUtils.readJson(userAction.getUploadedFiles(), UploadedFiles.class);

        if (existFiles == null) {
            existFiles = new UploadedFiles();
            existFiles.setTotalSize(.0);
            existFiles.setFiles(Lists.newArrayList());
        }

        List<UploadedFiles.FileRecord> existFileList = existFiles.getFiles();

        UploadedFiles.FileRecord newFile = UploadedFiles.FileRecord.valueOf(fileName, fileSize);

        existFileList.add(newFile);

        existFiles.setFiles(existFileList);
        existFiles.setTotalSize(existFiles.getTotalSize() + fileSize);

        userAction.setUser(user);
        userAction.setUploadedFiles(JacksonUtils.writeJson(existFiles));
        userActionRepository.save(userAction);
    }

    @Transactional
    public void removeUploadedFiles(String user, String fileName) {
        UserAction userAction = getUserAction(user);
        if (userAction != null && userAction.getUploadedFiles() != null) {
            UploadedFiles existFiles = JacksonUtils.readJson(userAction.getUploadedFiles(), UploadedFiles.class);
            if (existFiles == null || existFiles.getFiles() == null) return;
            List<UploadedFiles.FileRecord> record = Lists.newArrayList();
            existFiles.getFiles().forEach(f -> {
                if (f.getFileName().equals(fileName)) {
                    existFiles.setTotalSize(existFiles.getTotalSize() - f.getFileSize());
                } else {
                    record.add(f);
                }
            });
            existFiles.setFiles(record);
            userAction.setUploadedFiles(JacksonUtils.writeJson(existFiles));
            userActionRepository.save(userAction);
        }
    }
}
