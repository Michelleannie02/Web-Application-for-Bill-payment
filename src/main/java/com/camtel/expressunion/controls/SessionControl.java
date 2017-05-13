/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.camtel.expressunion.controls;



import com.camtel.expressunion.entities.Expressunion;
import com.camtel.expressunion.entities.Payitem;
import com.expressunion.services.ExpressunionFacadeLocal;
import com.huawei.cbs.ar.wsservice.bmpinterface.EUPAYBILL;
import com.huawei.cbs.ar.wsservice.bmpinterface.EUPAYBILLPortType;
import com.huawei.cbs.ar.wsservice.bmpinterface.PaymentRequest;
import com.huawei.cbs.ar.wsservice.bmpinterface.armsg.PaymentRequestMsg;
import com.huawei.cbs.ar.wsservice.bmpinterface.armsg.PaymentResultMsg;
import com.huawei.cbs.ar.wsservice.common.ResultHeader;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;




/**
 *
 * @author herma
 */
@Stateless
public class SessionControl {
    
    @EJB
    private ExpressunionFacadeLocal eufl;
    private   String nomFichierCharge=null;
    private   String lienFichierGenere;
    private   String lienFichierCharge;
    private   String path;
    private   String directoryName;
    private   String pathCopy;
    private   String rapport="rapport";
    private   String erreurpaiement="Erreur_Paiement_";
    
    private String disable = "true";
    private String error = "false";
   
    String price;
    String compteId;
    String transId;
    String numero;
    private String prefix = "Nombre de lignes traités :  ";
    private String numTreatment;
    
    private Date dateConvertible = new Date();
    DateFormat df = new SimpleDateFormat("dd.MM.yyyy ");
       
    private String dateFormat = df.format(dateConvertible);
    
    private List<Expressunion> paymentHistory = new ArrayList<>();
    private List<Payitem> listPaymentError = new ArrayList<>();
    
    
    
    private List<Payitem> listPayment = new ArrayList<>();
    private Payitem payValues = new Payitem();
    
    
    int nb_payment =0;
    int line_paiement=0;


    public SessionControl() throws MalformedURLException {
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    public void SessionControl() {
        
    }
    
    public void handleFileUpload(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succes", event.getFile().getFileName() + " est charge.");
        FacesContext.getCurrentInstance().addMessage(null, message);
        setNomFichierCharge(event.getFile().getFileName());
        
        File f = new File(getNomFichierCharge());
        lienFichierGenere = f.getAbsolutePath();
        
        System.out.println("lien fichiergenere:"+lienFichierGenere);
        
        try {
            createFolder();
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
        } catch (IOException e) {
        }
    }
    
    public void createFolder() {
    
    System.out.println("creation du dossier");
        
    path = System.getProperty("user.home") + File.separator + "Documents";
    
    directoryName = "Fichiers Batch Abonnés Camtel";
    
    File theDir  = new File(path+File.separator+directoryName);
    pathCopy =  path+File.separator+directoryName+File.separator;

// if the directory does not exist, create it
if (!theDir.exists()) {
    System.out.println("creating directory: " + directoryName);
    boolean result = false;

    try{
        theDir.mkdir();
        result = true;
    } 
    catch(SecurityException se){
        //handle it
    }        
    if(result) {    
        System.out.println("DIR created");  
    }
}
    }
    
    
    public void copyFile(String fileName, InputStream in) {
           try {
                // write the inputStream to a FileOutputStream
                OutputStream out = new FileOutputStream(new File(pathCopy + fileName));
             
                int read = 0;
                byte[] bytes = new byte[1024];
             
                while ((read = in.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
             
                in.close();
                out.flush();
                out.close();
             
                System.out.println("New file created!");
                } catch (IOException e) {
                System.out.println(e.getMessage());
                }
    }
    
    public void treatFile() throws FileNotFoundException {
        
        BufferedReader br = null;

       if(getNomFichierCharge() == null) {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Veuillez charger un fichier de type .csv"));
        } else {
        String fileToParse = pathCopy+getNomFichierCharge();
	   BufferedReader fileReader = null;
         
        //Delimiter used in CSV file
        final String DELIMITER = ",";
        try
        {
             String line;
            br = new BufferedReader(new FileReader(fileToParse));

            while ((line = br.readLine()) != null) {
                System.out.println(line);
                System.out.println("-------------");

                StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
                
                while (stringTokenizer.hasMoreElements()) {

                    transId = stringTokenizer.nextElement().toString();
		    compteId = stringTokenizer.nextElement().toString();
		    numero = stringTokenizer.nextElement().toString();
                    price = stringTokenizer.nextElement().toString();
                    
                    setValues(transId,numero,compteId,price);
                }

            }

            System.out.println("Successfully completed");
          //  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Succes de la procédure de paiement."));
        } 
        catch (Exception e) {
            e.printStackTrace();
        } 
         finally {
		try {
			if (br != null)
				br.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
     }
       
        payMyBill(listPayment);
        
        if (error.equals("true")) {
            
            disable = "false";
            
        }
  }

    
   // public void payMyBill (String transactionId,String idAccount,String numero, String amount) 
    public void payMyBill(List <Payitem> paymentList ) {
    int k =0;
    int sizeList = getListPayment().size();
    
    for(int i=0; i<sizeList ;i++)  {
      
      if (i==0) {
      } else {
          
      ResultHeader resultHeader = new ResultHeader();
      PaymentResultMsg resultPayment = new PaymentResultMsg();
           
   
      PaymentRequest paymentRequestValue = new PaymentRequest();
      PaymentRequestMsg paymentRequestMsg = new PaymentRequestMsg();
      
      //setting payment values
     
      paymentRequestValue.setInvoiceNo(paymentList.get(i).getInvoiceno());
      paymentRequestValue.setAcctId(paymentList.get(i).getAccountid());
      paymentRequestValue.setTransactionID(paymentList.get(i).getTranscid());
      paymentRequestValue.setApplicationCode("SITEMARCHE");
      paymentRequestValue.setPassword("CAMTEL");
      paymentRequestValue.setAmount(paymentList.get(i).getAmount());
      
      paymentRequestMsg.setPaymentRequest(paymentRequestValue);
      
 
      //déclaration du service
      EUPAYBILL paymentService = new EUPAYBILL();
      
      //déclaration de l'interface
      EUPAYBILLPortType paymentInterface = paymentService.getEUPAYBILLHttpSoap12Endpoint();
	
      resultPayment=paymentInterface.payment(paymentRequestMsg);
      
      String resultCode= resultPayment.getResultHeader().getResultCode().toString();
      
      System.out.println("result code :"+resultCode);
      String resultDesc= resultPayment.getResultHeader().getResultDesc().toString();
      System.out.println("result Dec :"+resultDesc);
      String comment= resultCode+": "+resultDesc;
     
  
      setHistoryOfPayment(paymentList.get(i), i,resultCode,comment);
      
      k=i;
    } 
      
  } 
        setNumTreatment(String.valueOf(k));
    FacesMessage message = new FacesMessage("Fin de  la transaction! Veuillez consulter le rapport");
    FacesContext.getCurrentInstance().addMessage(null, message);

 }
    
    public void changeDisable () {
        
        disable = "true";
    }
    
    
    public void setHistoryOfPayment( Payitem infoPayment,int line,String resultCode,String comment) {
         
      
          
        Expressunion infosReponse = new Expressunion();
   
        infosReponse.setFileName(nomFichierCharge);
        infosReponse.setLine(String.valueOf(line));
        infosReponse.setNumTrans(infoPayment.getTranscid());
        if(resultCode.equalsIgnoreCase("0")) {
          infosReponse.setStatutTrans("succes");
          paymentHistory.add(infosReponse);
        } else {
          error = "true";  
          infosReponse.setStatutTrans("failed");
          paymentHistory.add(infosReponse);
          setErrorPayment (infoPayment,line,"failed",comment);  
            
        }
}
    
    public void setErrorPayment (Payitem infoPayment,int line,String statut, String comment) {
        
        Expressunion infosError = new Expressunion();
        Payitem errorPayment = new Payitem();
   
        infosError.setFileName(nomFichierCharge);
        infosError.setLine(String.valueOf(line));
        infosError.setNumTrans(infoPayment.getTranscid());
        infosError.setStatutTrans(statut);
        infosError.setCommentaire(comment);
        
        errorPayment.setTranscid(infoPayment.getTranscid());
        errorPayment.setAmount(infoPayment.getAmount());
        errorPayment.setInvoiceno(infoPayment.getInvoiceno());
        errorPayment.setAccountid(infoPayment.getAccountid());
        
        paymentHistory.add(infosError);
        eufl.create(infosError);
        listPaymentError.add(errorPayment);

    }
    
public int lineCount(String fileName) throws FileNotFoundException, IOException {
    
    int count=0;
    
     InputStream is = new BufferedInputStream(new FileInputStream(fileName));
    try {
        byte[] c = new byte[1024];
        int readChars = 0;
        boolean empty = true;
        while ((readChars = is.read(c)) != -1) {
            empty = false;
            for (int i = 0; i < readChars; ++i) {
                if (c[i] == '\n') {
                    ++count;
                }
            }
        }
        return (count == 0 && !empty) ? 1 : count;
    } finally {
        is.close();
    }
}

public void setValues (String transId, String invoice,String account, String price) {
           Payitem payVal = new Payitem();
           
        payVal .setAmount(price);
        payVal .setInvoiceno(invoice);
        payVal .setTranscid(transId);
        payVal .setAccountid(account);
        
     
        getListPayment().add(payVal);
}


// Traitement fichier pdf

  public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException {
   
        Document pdf = (Document) document;
        pdf.open();
        pdf.setPageSize(PageSize.A4);
        
 
     // ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String logo = "C:\\www\\ExpressUnion\\ExpressUnion\\src\\main\\webapp\\images\\logo-new-camtel2.png";
        pdf.add(Image.getInstance(logo));
      
        pdf.addTitle("Rapport de l'opération!");
     
        
        
    }

public void postProcessPDF(Object document) {
      
    /*  String sourceFile= "C:\\Users\\herma\\Downloads\\Documents\\"; 
      String fileName = patientToConsult.getNomPatient()+dateFormatteConsult+".pdf";
      
      File linkFile = new File(sourceFile+fileName);
      File destination = new File(linkToRepConsultation+patientToConsult.getCniPatient()+".pdf");
      System.out.print("on a traversÃƒÂ© la mÃƒÂ©thode de renommage");
      linkFile.renameTo(destination);*/
    
   // pdf.addCreationDate();
}

    /**
     * @return the paymentHistory
     */
    public List<Expressunion> getPaymentHistory() {
        return paymentHistory;
    }

    /**
     * @param paymentHistory the paymentHistory to set
     */
    public void setPaymentHistory(List<Expressunion> paymentHistory) {
        this.paymentHistory = paymentHistory;
    }

    /**
     * @return the dateConvertible
     */
    public Date getDateConvertibleForConsult() {
        return dateConvertible;
    }

    /**
     * @param dateConvertibleForConsult the dateConvertible to set
     */
    public void setDateConvertibleForConsult(Date dateConvertibleForConsult) {
        this.dateConvertible = dateConvertibleForConsult;
    }

    /**
     * @return the nomFichierCharge
     */
    public String getNomFichierCharge() {
        return nomFichierCharge;
    }

    /**
     * @param nomFichierCharge the nomFichierCharge to set
     */
    public void setNomFichierCharge(String nomFichierCharge) {
        this.nomFichierCharge = nomFichierCharge;
    }

    /**
     * @return the dateFormat
     */
    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * @param dateFormat the dateFormat to set
     */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * @return the rapport
     */
    public String getRapport() {
        return rapport;
    }

    /**
     * @param rapport the rapport to set
     */
    public void setRapport(String rapport) {
        this.rapport = rapport;
    }

    /**
     * @return the listPayment
     */
    public List<Payitem> getListPayment() {
        return listPayment;
    }

    /**
     * @param listPayment the listPayment to set
     */
    public void setListPayment(List<Payitem> listPayment) {
        this.listPayment = listPayment;
    }

    /**
     * @return the payValues
     */
    public Payitem getPayValues() {
        return payValues;
    }

    /**
     * @param payValues the payValues to set
     */
    public void setPayValues(Payitem payValues) {
        this.payValues = payValues;
    }

    /**
     * @return the eufl
     */
    public ExpressunionFacadeLocal getEufl() {
        return eufl;
    }

    /**
     * @param eufl the eufl to set
     */
    public void setEufl(ExpressunionFacadeLocal eufl) {
        this.eufl = eufl;
    }

    /**
     * @return the listPaymentError
     */
    public List<Payitem> getListPaymentError() {
        return listPaymentError;
    }

    /**
     * @param listPaymentError the listPaymentError to set
     */
    public void setListPaymentError(List<Payitem> listPaymentError) {
        this.listPaymentError = listPaymentError;
    }

    /**
     * @return the erreurpaiement
     */
    public String getErreurpaiement() {
        return erreurpaiement;
    }

    /**
     * @param erreurpaiement the erreurpaiement to set
     */
    public void setErreurpaiement(String erreurpaiement) {
        this.erreurpaiement = erreurpaiement;
    }

    /**
     * @return the disable
     */
    public String getDisable() {
        return disable;
    }

    /**
     * @param disable the disable to set
     */
    public void setDisable(String disable) {
        this.disable = disable;
    }

    /**
     * @return the numTreatment
     */
    public String getNumTreatment() {
        return numTreatment;
    }

    /**
     * @param numTreatment the numTreatment to set
     */
    public void setNumTreatment(String numTreatment) {
        this.numTreatment = numTreatment;
    }

    /**
     * @return the prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * @param prefix the prefix to set
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    
}
 

