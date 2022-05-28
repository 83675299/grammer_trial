import java.util.*;

public class Production {
    String leftPart="";
    List<String> rightParts = new ArrayList<String>();
    public Production(String s){
        Scan(s);
    }
    public void Scan(String s){
        int i=0;
        boolean left=true;
        while(i<s.length()){
            boolean f =true;//ÅÐ¶ÏÊÇ·ñÓöµ½ÌØÊâ·ûºÅ£¨²»°üÀ¨->£©
            String indexString = "";
            if(s.charAt(i)=='-'&&s.charAt(i+1)=='>'){
                i+=2;
                left=false;
                f=false;
                continue;
            }
            while(s.charAt(i)>='a'&&s.charAt(i)<='z'||s.charAt(i)>='A'&&s.charAt(i)<='Z'){
                f=false;
                indexString+=s.charAt(i);
                i++;
                if(i==s.length()){
                    break;
                }
            }
            if(f){
                if(s.charAt(i)==' '){
                    i++;
                    continue;
                }
                else{
                    indexString+=s.charAt(i);
                    rightParts.add(indexString);
                    i++;
                    continue;
                }
            }
            if(left){
                leftPart=indexString;
            }
            else{
                rightParts.add(indexString);
            }
        }
    }
    public String getLeftPart(){
        return leftPart;
    }
    public List<String> getRightParts(){
        return rightParts;
    }
}
