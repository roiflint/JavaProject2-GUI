package com;

public class ErrorDto {
    private boolean Success;
    private String Message;

    public ErrorDto(boolean success, String message){
        this.Message = message;
        this.Success = success;
    }
    public ErrorDto(){
        this.Message = "";
        this.Success = true;
    }

    public void setMessage(String message){this.Message=message;}
    public void setSuccess(boolean success){this.Success = success;}
    public boolean getSuccess(){return this.Success;}
    public String getMessage(){return this.Message;}
}
