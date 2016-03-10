package communication;

/**
 *
 * @author etude
 */
public class Message 
{
    private String type;
    private Object objet;
    
    public Message(String parType, Object parObjet)
    {
        this.type = parType;
        this.objet = parObjet;
    }
}
