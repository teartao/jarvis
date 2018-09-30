package com.demon.bean;

import org.springframework.beans.propertyeditors.CustomCollectionEditor;

public class FactoryListEditor extends CustomCollectionEditor {
    
    public FactoryListEditor() {
        super(FactoryArrayList.class);
    }

}