package com.kathesama.apirestspringtemplate.apirestspringtemplate.exception;

public class GenericCollectionException extends Exception{
    public static final long serialVersionUID = 1L;

    public GenericCollectionException(String message){
        super(message);
    }

    public static String NotFound(String id){
        return "Element with " + id + " not found";
    }

    public static String TodoAlreadyExists(){
        return "Element with given name already exists in database";
    }
    public static String notEmpty(){
        return "Element with given name can't be empty";
    }
}
