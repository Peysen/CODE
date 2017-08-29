package com.pey.quartz;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by Peysen on 2017/7/26.
 */
public class FileExtensionFileFilter implements FileFilter {
    private  String extension;//文件后缀

    public FileExtensionFileFilter(String extension)
    {
        this.extension = extension;
    }
    public   boolean  accept(File file)
    {//只接受指定后缀的文件
        // Lowercase the filename for easier comparison
        String lCaseFilename = file.getName().toLowerCase();//小写化
        return  (file.isFile() &&(lCaseFilename.indexOf(extension) >  0 )) ?  true : false ;
    }
}
