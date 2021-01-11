package dto;


public class CatStatusDTO {
    public boolean verified;
    public int sentCount;

    public CatStatusDTO(boolean verified, int sentCount) {
        this.verified = verified;
        this.sentCount = sentCount;
    }
}
