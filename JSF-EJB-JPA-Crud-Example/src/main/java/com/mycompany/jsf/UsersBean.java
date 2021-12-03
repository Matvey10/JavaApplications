package com.mycompany.jsf;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import com.mycompany.dao.UsersEJB;
import com.mycompany.entity.Users;

@ManagedBean(name="users")
@RequestScoped
public class UsersBean {
    
    FacesContext context = FacesContext.getCurrentInstance();

    private Users user;
    
    @EJB
    UsersEJB userEJB;
    
    private List<Users> userList = new ArrayList();
    
    @PostConstruct
    public void getAllUsersList(){
        user = new Users();
        userList = userEJB.allUsers();

    }
    public String editUserss(Users userrrr){
        this.user = userrrr;
        return "edit.xhtml";
    }
    public String editUser(){
        System.out.println("--------------------1--------------------------------");
        System.out.println("--------------------1--------------------------------");
        System.out.println(this.user.getId());
        System.out.println("--------------------1--------------------------------");
        System.out.println("--------------------1--------------------------------");

        
        if(!this.user.getEmail().contains("@") || !this.user.getEmail().contains("."))
        {
            context.addMessage(null, new FacesMessage("Invalid e-mail address"));
            return null;
        }

        try
        {
        System.out.println("--------------------1--------------------------------");
        System.out.println("---------------------2-------------------------------");
            userEJB.editUsers(this.user);
            
        System.out.println("--------------------3--------------------------------");
            userList = userEJB.allUsers();
            
        System.out.println("---------------------4-------------------------------");
            return "index.xhtml?faces-redirect=true";
        }
        catch(Exception e)
        {
            context.addMessage(null, new FacesMessage("Error Updating User ... \n "+e));
            return null;
        }
        
    }
    
    public String addUser(){
        try
        {
            if(!user.getEmail().contains("@") || !user.getEmail().contains("."))
            {
                context.addMessage(null, new FacesMessage("Not a valid email"));
                return null;
            }
                
            
            user = userEJB.saveUser(user);
            
            userList = userEJB.allUsers();
            
            context.addMessage(null, new FacesMessage("User Added Successfully ..."));
            return "index.xhtml?faces-redirect=true";
        }
        catch(Exception e)
        {
            context.addMessage(null, new FacesMessage("Error Adding User ... \n "+e));
            return null;
        }
    }

    public String deleteUser(Users getUser){
        try
        {
            userEJB.deleteUsers(getUser);
            userList = userEJB.allUsers();
            
            context.addMessage(null, new FacesMessage("User Successfully Deleted ..."));
            return "index.xhtml?faces-redirect=true";
        }
        catch(Exception e)
        {
            context.addMessage(null, new FacesMessage("Error Deleting User ... \n "+e));
            return null;
        }
    }
    
    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public List<Users> getUserList() {
        return userList;
    }

    public void setUserList(List<Users> userList) {
        this.userList = userList;
    }
    
}
