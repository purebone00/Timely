package master;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import controller.EmployeeBean;
import model.Employee;

@Named("Login")
@RequestScoped
public class Login implements Serializable {
    @Inject EmployeeBean empBean;
    
    private String userName;
    
    private String password;
    
    private List<Employee> list ;
    
    public Login() {
        
    }
    
    public boolean authUser() {
        list = empBean.getList();
        boolean authenticated = false;
        
        for(int i = 0; i < list.size(); i++) {
            Employee employee = list.get(i);
            if(employee.getEmpLnm().equals(userName) && employee.getEmpPw().equals(password)) {
                authenticated = empBean.find(employee.getEmpId());
            }
        }
        return authenticated;
    }
    
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
    public List<Employee> getList() {
        return list;
    }
    
    public void setList(List<Employee> newList) {
        this.list = newList;
    }
}
