package actions;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import model.Usuario;
import model.UsuarioDAO;
import org.apache.struts2.ServletActionContext;

/**
 *
 * @author Débora
 */
public class DeleteFolderAction extends ActionSupport {
    
    private String filePath;
    private long storage;

    public String execute() throws UnsupportedEncodingException {
        //pega a requisição http do servlet
        HttpServletRequest request = ServletActionContext.getRequest();
        Map<String,Object> session = ActionContext.getContext().getSession();
        
        //Pega o usuario salvo na sessao
        UsuarioDAO dao = new UsuarioDAO();
        int id = (int) session.get("id");
        Usuario u = dao.getUserById(id);    

        try {
            //checa se já existe um path 
            if (filePath != null) {
                filePath = request.getServletContext().getRealPath(u.getDir()+filePath);
                System.out.println(filePath+" -- delete folder ------------------ -------------------------");
                File folderDelete = new File(filePath);   
                storage = u.getStorage();
                System.out.println(storage+" -- storage u.getStorage ------------------ -------------------------"); 
                delete(folderDelete);

                System.out.println(storage+" -- stprage depois de deletar------------------ -------------------------");
                u.setStorage(storage);               
                dao.updateUser(u);
                
                return "sucess";
            }            
            return "error";
        } catch (Exception ex) {
            return "error";
        }     
    }
    
    public void delete(File path){
        File[] l = path.listFiles();
        for (File f : l){
            if (f.isDirectory())
                delete(f);
            else {
                setStorage(getStorage() - f.length());
                f.delete();
            }
        }
        path.delete(); 
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getStorage() {
        return storage;
    }

    public void setStorage(long storage) {
        this.storage = storage;
    }
    
    
}