import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Grammer {
    private List<First> firsts=new ArrayList<First>();
    private List<Follow> follows=new ArrayList<Follow>();

    private List<Production> productions=new ArrayList<Production>();
    public List<Production> getProductions(){
        return productions;
    }
    private List<String> nonterminals= new ArrayList<String>();//非终结符
    public Grammer(String path){
        try{
            FileReader fr = new FileReader("grammer.txt");
            BufferedReader bf = new BufferedReader(fr);//读文件
            String s = bf.readLine();
            while(s!=null){
                System.out.println(s);
                Production production=new Production(s);
                System.out.println("产生式是"+production.leftPart+production.rightParts);
                productions.add(production);
                s = bf.readLine();
            }
//            s=productions.get(0).leftPart+"'->"+productions.get(0).leftPart;//增加产生式S'->S
//            Production production=new Production(s);
//            productions.add(0,production);
        } catch (IOException e) {
            System.out.println("文件打不开，出错");
        }
        createNonterminals();
        for(int i=0;i<nonterminals.size();i++)
            System.out.println("非终结符是"+nonterminals.get(i));
        createFirsts();
        createFollow();
        for(int i=0;i<firsts.size();i++)
        System.out.println("first集"+i+"是"+firsts.get(i).getFirst());
        for(int i=0;i<follows.size();i++)
        System.out.println("follow集"+i+"是"+follows.get(i).getFollow());
        System.out.println(follows.get(0));
    }
    public void createNonterminals(){//创建非终结符集
        int i=0;
        while(i<productions.size()){
            if(!nonterminals.contains(productions.get(i).leftPart)){
                nonterminals.add(productions.get(i).leftPart);
            }
            i++;
        }
    }

    public void createFirsts(){
        boolean f=true;//判断firsts是否被修改，如果被修改过，那就再循环一次
        for (int i = 0; i < nonterminals.size(); i++) {
            firsts.add(new First()); // 为firsts集合分配空间
        }
        while(f){
            f=false;//此时firsts未被修改
            for(int i=0;i<nonterminals.size();i++){
                for(int j=0;j<productions.size();j++){
                    String nt=nonterminals.get(i);
                    Production pd=productions.get(j);
                    if(nt.equals(pd.leftPart)){
                        for(int k=0;k<pd.rightParts.size();k++){
                            if(!nonterminals.contains(pd.rightParts.get(k))){//如果产生式右边第k个不是非终结符，看first集里有没有该符，没有就加进去
                                if(!firsts.get(i).getFirst().contains(pd.rightParts.get(k))){
                                    firsts.get(i).getFirst().add(pd.rightParts.get(k));
                                    f=true;
                                }
                            }
                            else{//如果产生式右边第k个是非终结符，先把k的first加进去，再看它是否可空，可空就继续找k+1
                                for(int u=0;u<firsts.get(nonterminals.indexOf(pd.rightParts.get(k))).getFirst().size();u++){//把该非终结符的first集全加入到first(i)中

                                    if(!firsts.get(i).getFirst().contains(firsts.get(nonterminals.indexOf(pd.rightParts.get(k))).getFirst().get(u))){
                                        firsts.get(i).getFirst().add(firsts.get(nonterminals.indexOf(pd.rightParts.get(k))).getFirst().get(u));
                                        f=true;
                                    }
                                }
                                if(firsts.get(nonterminals.indexOf(pd.rightParts.get(k))).getFirst().contains('#')){
                                    ;//补
                                }
                                else break;
                            }
                        }
                    }
                }
            }
        }
    }
    public void createFollow(){
        boolean f=true,non=true;//f是用来判断在这个循环所有follow集是否有变动，有变动就再进行一个循环，直到没有变动，non是判断第j个符号后面的非终结符号的first集是否可空
        for (int i = 0; i < nonterminals.size(); i++) {
            follows.add(new Follow()); // 为follows集合分配空间
        }
        follows.get(0).getFollow().add("$");
        while(f){
            f=false;
            for(int i=0;i<productions.size();i++){//第i个产生式
                for(int j=0;j<productions.get(i).rightParts.size()-1;j++){//在产生式的右边，如果非终结符不是最后一个，那就把它后面的first集加入到它的follow集中，产生式右边第j个符号
                    String rp=productions.get(i).rightParts.get(j);//产生式右边第j个符号
                    if(nonterminals.contains(rp)&&j<productions.get(i).rightParts.size()-1){//如果第j个符号是非终结符
                        non=true;
                        for(int u=j+1;u<productions.get(i).rightParts.size();u++){//判断第j个符号后面是否可空
                            //System.out.println(u+"+++"+productions.get(i).rightParts.get(u));
                            if(nonterminals.contains(productions.get(i).rightParts.get(u))){
                                if(!firsts.get(nonterminals.indexOf(productions.get(i).rightParts.get(u))).getFirst().contains('#')){
                                    //System.out.println(111);
                                    non=false;
                                    if(!follows.get(nonterminals.indexOf(rp)).getFollow().contains(firsts.get(nonterminals.indexOf((productions.get(i).rightParts.get(u)))).getFirst().get(u))){
                                        follows.get(nonterminals.indexOf(rp)).getFollow().add(firsts.get(nonterminals.indexOf((productions.get(i).rightParts.get(u)))).getFirst().get(u));
                                        f=true;
                                        //System.out.println(firsts.get(nonterminals.indexOf((productions.get(i).rightParts.get(u)))).getFirst().get(u));
                                        //System.out.println(i+"+"+j+"+"+u);
                                    }
                                    //System.out.println(222);

                                    break;
                                }
                            }
                            else{
                                if(!follows.get(nonterminals.indexOf(rp)).getFollow().contains(productions.get(i).rightParts.get(j+1))){
                                    follows.get(nonterminals.indexOf(rp)).getFollow().add(productions.get(i).rightParts.get(j+1));
                                    //System.out.println(productions.get(i).rightParts.get(j+1));
                                    f=true;
                                    non=false;
                                }
                                break;
                            }
                        }
                        if(non==false){//第j个符号后面的非终结符号的first集都是非空或都是终结符号
                            if(nonterminals.contains(productions.get(i).rightParts.get(j+1))){
                                System.out.println(333);
                                for(int k=0;k<firsts.get(nonterminals.indexOf(productions.get(i).rightParts.get(j+1))).getFirst().size();k++){
                                    if(!follows.get(nonterminals.indexOf(rp)).getFollow().contains(firsts.get(nonterminals.indexOf((productions.get(i).rightParts.get(j+1)))).getFirst().get(k))){
                                        follows.get(nonterminals.indexOf(rp)).getFollow().add(firsts.get(nonterminals.indexOf((productions.get(i).rightParts.get(j+1)))).getFirst().get(k));
                                        f=true;
                                    }
                                }
                            }

//                            if(firsts.get(nonterminals.indexOf(productions.get(i).rightParts.get(j+1))).getFirst().contains('#')&&j<productions.get(i).rightParts.size()-2){
//                                String s=productions.get(i).rightParts.get(j+2);
//                                follows.get(nonterminals.indexOf(rp)).getFollow().add(s);
//                            }
                        }
                        else {
                            for(int k=0;k<follows.get(nonterminals.indexOf(productions.get(i).leftPart)).getFollow().size();k++){
                                if(!follows.get(nonterminals.indexOf(rp)).getFollow().contains(follows.get(nonterminals.indexOf(productions.get(i).leftPart)).getFollow().get(k))){
                                    follows.get(nonterminals.indexOf(rp)).getFollow().add(follows.get(nonterminals.indexOf(productions.get(i).leftPart)).getFollow().get(k));
                                    f=true;
                                }
                            }
                        }
                    }
                }
                if(nonterminals.contains(productions.get(i).rightParts.get(productions.get(i).rightParts.size()-1))){
                    for(int k=0;k<follows.get(nonterminals.indexOf(productions.get(i).leftPart)).getFollow().size();k++){
                        if(!follows.get(nonterminals.indexOf(productions.get(i).rightParts.get(productions.get(i).rightParts.size()-1))).getFollow().contains(follows.get(nonterminals.indexOf(productions.get(i).leftPart)).getFollow().get(k))){
                            follows.get(nonterminals.indexOf(productions.get(i).rightParts.get(productions.get(i).rightParts.size()-1))).getFollow().add(follows.get(nonterminals.indexOf(productions.get(i).leftPart)).getFollow().get(k));
                            f=true;
                    }

                }

                }
            }
        }
    }
}

