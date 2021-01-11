
package dto;


public class CatFactDTO {
    
    public CatStatusDTO status;
    public String type;
    public boolean deleted;
    public String _id;
    public String user;
    public String text;
    public String source;
    public String updatedAt;
    public String createdAt;
    public boolean used;

    public CatFactDTO(CatStatusDTO status, String type, boolean deleted, String _id, String user, String text, String source, String updatedAt, String createdAt, boolean used) {
        this.status = status;
        this.type = type;
        this.deleted = deleted;
        this._id = _id;
        this.user = user;
        this.text = text;
        this.source = source;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.used = used;
    }
    
    
    
    
    
    
    
}
