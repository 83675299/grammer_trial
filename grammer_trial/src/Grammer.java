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
    private List<String> nonterminals= new ArrayList<String>();//���ս��
    public Grammer(String path){
        try{
            FileReader fr = new FileReader("grammer.txt");
            BufferedReader bf = new BufferedReader(fr);//���ļ�
            String s = bf.readLine();
            while(s!=null){
                System.out.println(s);
                Production production=new Production(s);
                System.out.println("����ʽ��"+production.leftPart+production.rightParts);
                productions.add(production);
                s = bf.readLine();
            }
//            s=productions.get(0).leftPart+"'->"+productions.get(0).leftPart;//���Ӳ���ʽS'->S
//            Production production=new Production(s);
//            productions.add(0,production);
        } catch (IOException e) {
            System.out.println("�ļ��򲻿�������");
        }
        createNonterminals();
        for(int i=0;i<nonterminals.size();i++)
            System.out.println("���ս����"+nonterminals.get(i));
        createFirsts();
        createFollow();
        for(int i=0;i<firsts.size();i++)
        System.out.println("first��"+i+"��"+firsts.get(i).getFirst());
        for(int i=0;i<follows.size();i++)
        System.out.println("follow��"+i+"��"+follows.get(i).getFollow());
        System.out.println(follows.get(0));
    }
    public void createNonterminals(){//�������ս����
        int i=0;
        while(i<productions.size()){
            if(!nonterminals.contains(productions.get(i).leftPart)){
                nonterminals.add(productions.get(i).leftPart);
            }
            i++;
        }
    }

    public void createFirsts(){
        boolean f=true;//�ж�firsts�Ƿ��޸ģ�������޸Ĺ����Ǿ���ѭ��һ��
        for (int i = 0; i < nonterminals.size(); i++) {
            firsts.add(new First()); // Ϊfirsts���Ϸ���ռ�
        }
        while(f){
            f=false;//��ʱfirstsδ���޸�
            for(int i=0;i<nonterminals.size();i++){
                for(int j=0;j<productions.size();j++){
                    String nt=nonterminals.get(i);
                    Production pd=productions.get(j);
                    if(nt.equals(pd.leftPart)){
                        for(int k=0;k<pd.rightParts.size();k++){
                            if(!nonterminals.contains(pd.rightParts.get(k))){//�������ʽ�ұߵ�k�����Ƿ��ս������first������û�и÷���û�оͼӽ�ȥ
                                if(!firsts.get(i).getFirst().contains(pd.rightParts.get(k))){
                                    firsts.get(i).getFirst().add(pd.rightParts.get(k));
                                    f=true;
                                }
                            }
                            else{//�������ʽ�ұߵ�k���Ƿ��ս�����Ȱ�k��first�ӽ�ȥ���ٿ����Ƿ�ɿգ��ɿվͼ�����k+1
                                for(int u=0;u<firsts.get(nonterminals.indexOf(pd.rightParts.get(k))).getFirst().size();u++){//�Ѹ÷��ս����first��ȫ���뵽first(i)��

                                    if(!firsts.get(i).getFirst().contains(firsts.get(nonterminals.indexOf(pd.rightParts.get(k))).getFirst().get(u))){
                                        firsts.get(i).getFirst().add(firsts.get(nonterminals.indexOf(pd.rightParts.get(k))).getFirst().get(u));
                                        f=true;
                                    }
                                }
                                if(firsts.get(nonterminals.indexOf(pd.rightParts.get(k))).getFirst().contains('#')){
                                    ;//��
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
        boolean f=true,non=true;//f�������ж������ѭ������follow���Ƿ��б䶯���б䶯���ٽ���һ��ѭ����ֱ��û�б䶯��non���жϵ�j�����ź���ķ��ս���ŵ�first���Ƿ�ɿ�
        for (int i = 0; i < nonterminals.size(); i++) {
            follows.add(new Follow()); // Ϊfollows���Ϸ���ռ�
        }
        follows.get(0).getFollow().add("$");
        while(f){
            f=false;
            for(int i=0;i<productions.size();i++){//��i������ʽ
                for(int j=0;j<productions.get(i).rightParts.size()-1;j++){//�ڲ���ʽ���ұߣ�������ս���������һ�����ǾͰ��������first�����뵽����follow���У�����ʽ�ұߵ�j������
                    String rp=productions.get(i).rightParts.get(j);//����ʽ�ұߵ�j������
                    if(nonterminals.contains(rp)&&j<productions.get(i).rightParts.size()-1){//�����j�������Ƿ��ս��
                        non=true;
                        for(int u=j+1;u<productions.get(i).rightParts.size();u++){//�жϵ�j�����ź����Ƿ�ɿ�
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
                        if(non==false){//��j�����ź���ķ��ս���ŵ�first�����Ƿǿջ����ս����
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

