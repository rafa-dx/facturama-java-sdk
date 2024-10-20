
import com.Facturama.sdk_java.Container.FacturamaApi;
import com.Facturama.sdk_java.Container.FacturamaApiMultiemisor;
import com.Facturama.sdk_java.Models.Exception.FacturamaException;
import com.Facturama.sdk_java.Models.Csd;
import com.Facturama.sdk_java.Models.Request.CfdiType;
import com.Facturama.sdk_java.Models.Request.Complements.Complements;
import com.Facturama.sdk_java.Models.Request.Complements.INE.IdContabilidad;
import com.Facturama.sdk_java.Models.Request.Complements.INE.Ine;
import com.Facturama.sdk_java.Models.Request.Complements.INE.IneEntidad;
import com.Facturama.sdk_java.Models.Request.Issuer;
import com.Facturama.sdk_java.Models.Request.Item;
import com.Facturama.sdk_java.Models.Request.Payment;
import com.Facturama.sdk_java.Models.Request.Receiver;
import com.Facturama.sdk_java.Models.Request.GlobalInformation;
import com.Facturama.sdk_java.Models.Request.RelatedDocument;
import com.Facturama.sdk_java.Models.Request.Tax;
import com.Facturama.sdk_java.Models.Request.ThirdPartyAccount;
import com.Facturama.sdk_java.Models.Response.CancelationStatus;
import com.Facturama.sdk_java.Models.Response.Catalogs.Catalog;
import com.Facturama.sdk_java.Models.Response.Catalogs.Cfdi.Currency;
import com.Facturama.sdk_java.Models.Response.Catalogs.Cfdi.NameCfdi;
import com.Facturama.sdk_java.Models.Response.CfdiSearchResult;
import com.Facturama.sdk_java.Services.CfdiService;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.Facturama.sdk_java.Models.Response.Cfdi;
import java.util.Random;

/*
 * Soporte API
 * @author Facturama
 * facturamasoporte@freshbooks.com
 */
public class SampleApiMultiemisor {

    public static void principal() {
        System.out.println("Ejemplos de consumo de la FacturamaAPI Multiemisor, con el usuario de 'pruebas'");

        try {

            FacturamaApiMultiemisor facturama = createApiInstance();

            // Ejemplo de administración de CSDs (descomenta la linea para incluirlo en la ejecución)
            //sampleCsd(facturama);
            // Ejemplo de creación de CFDI 4.0
            //sampleCfdi40(facturama);
            //Test Cancelación
            //TestCancel(facturama);
            // Ejemplo de creación de "Complemento de Pago"
            samplePaymentComplement(facturama);

        } catch (FacturamaException ex) {
            // Se muestran los errores
            System.out.println(ex.getMessage());

            Map<String, String[]> messageDetail = ex.getModel().getDetails();
            messageDetail.entrySet().forEach((entry) -> {
                System.out.println(entry.getKey() + ": " + String.join(",", entry.getValue()));
            });

        } catch (Exception ex) {
            System.out.println("Error inesperado: " + ex.getMessage());
        }

    }

    /*
     * Creación del Objeto Facturama Multiemisor
     * @return Objeto creado, listo para hacer las peticiones a la API
     */
    private static FacturamaApiMultiemisor createApiInstance() {
        String user = "sdkpruebas";
        String password = "pruebas2022";
        Boolean isDevMode = true;       // true  = Sandbox, false = Productivo

        return new FacturamaApiMultiemisor(user, password, isDevMode);
    }

    /*
     * Ejemplo de manejo de los Sellos Digitales CSD
     * - Eliminar
     * - Agregar
     * - Listar
     * - Mostrar CSD específico
     * 
     * @param facturama Objeto Facturama Multiemisor
     * @throws IOException
     * @throws FacturamaException
     * @throws Exception 
     */
    private static void sampleCsd(FacturamaApiMultiemisor facturama) throws IOException, FacturamaException, Exception {

        System.out.println("----- Inicio del ejemplo de CSD -----");

        System.out.println("Eliminando el Certificado para el RFC EKU9003173C9");

        facturama.Csd().Remove("EKU9003173C9");

        System.out.println("Agregando el Certificado para el RFC EKU9003173C9");
        sampleCsdCreate(facturama);

        // Listado de todos los Certificados que tiene cargados el usuario       
//        System.out.println( "Listado de todos los Certificados que tiene cargados el usuario" );
//        List<Csd> lstCsd = facturama.Csd().List();         
//       for(int i=0;i <lstCsd.size();i++)
//       {
//            Csd csdlist = lstCsd.get(i);
//            System.out.println();
//            System.out.println(csdlist.getCertificate());
//            System.out.println(csdlist.getPrivateKey());
//            System.out.println(csdlist.getPrivateKeyPassword());
//            System.out.println(csdlist.getRfc());                   
//       }
// 
//       // Mostrado del certificado especìfico para el RFC AAA010101AAA
//        System.out.println( "Mostrado del certificado especìfico para el RFC EKU9003173C9" );
//        System.out.println();
//        System.out.println(facturama.Csd().Retrieve("EKU9003173C9").getCertificate());
//        System.out.println(facturama.Csd().Retrieve("EKU9003173C9").getPrivateKey());
//        System.out.println(facturama.Csd().Retrieve("EKU9003173C9").getPrivateKeyPassword());
//        System.out.println(facturama.Csd().Retrieve("EKU9003173C9").getRfc());
//        
//        System.out.println();
//        System.out.println("ejemplo de CSD terminado");
    }

    private static Csd sampleCsdCreate(FacturamaApiMultiemisor facturama) throws IOException, FacturamaException, Exception {
        Csd newCsd = new Csd();
        newCsd.setCertificate("MIIFsDCCA5igAwIBAgIUMzAwMDEwMDAwMDA1MDAwMDM0MTYwDQYJKoZIhvcNAQELBQAwggErMQ8wDQYDVQQDDAZBQyBVQVQxLjAsBgNVBAoMJVNFUlZJQ0lPIERFIEFETUlOSVNUUkFDSU9OIFRSSUJVVEFSSUExGjAYBgNVBAsMEVNBVC1JRVMgQXV0aG9yaXR5MSgwJgYJKoZIhvcNAQkBFhlvc2Nhci5tYXJ0aW5lekBzYXQuZ29iLm14MR0wGwYDVQQJDBQzcmEgY2VycmFkYSBkZSBjYWxpejEOMAwGA1UEEQwFMDYzNzAxCzAJBgNVBAYTAk1YMRkwFwYDVQQIDBBDSVVEQUQgREUgTUVYSUNPMREwDwYDVQQHDAhDT1lPQUNBTjERMA8GA1UELRMIMi41LjQuNDUxJTAjBgkqhkiG9w0BCQITFnJlc3BvbnNhYmxlOiBBQ0RNQS1TQVQwHhcNMjMwNTE4MTE0MzUxWhcNMjcwNTE4MTE0MzUxWjCB1zEnMCUGA1UEAxMeRVNDVUVMQSBLRU1QRVIgVVJHQVRFIFNBIERFIENWMScwJQYDVQQpEx5FU0NVRUxBIEtFTVBFUiBVUkdBVEUgU0EgREUgQ1YxJzAlBgNVBAoTHkVTQ1VFTEEgS0VNUEVSIFVSR0FURSBTQSBERSBDVjElMCMGA1UELRMcRUtVOTAwMzE3M0M5IC8gVkFEQTgwMDkyN0RKMzEeMBwGA1UEBRMVIC8gVkFEQTgwMDkyN0hTUlNSTDA1MRMwEQYDVQQLEwpTdWN1cnNhbCAxMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtmecO6n2GS0zL025gbHGQVxznPDICoXzR2uUngz4DqxVUC/w9cE6FxSiXm2ap8Gcjg7wmcZfm85EBaxCx/0J2u5CqnhzIoGCdhBPuhWQnIh5TLgj/X6uNquwZkKChbNe9aeFirU/JbyN7Egia9oKH9KZUsodiM/pWAH00PCtoKJ9OBcSHMq8Rqa3KKoBcfkg1ZrgueffwRLws9yOcRWLb02sDOPzGIm/jEFicVYt2Hw1qdRE5xmTZ7AGG0UHs+unkGjpCVeJ+BEBn0JPLWVvDKHZAQMj6s5Bku35+d/MyATkpOPsGT/VTnsouxekDfikJD1f7A1ZpJbqDpkJnss3vQIDAQABox0wGzAMBgNVHRMBAf8EAjAAMAsGA1UdDwQEAwIGwDANBgkqhkiG9w0BAQsFAAOCAgEAFaUgj5PqgvJigNMgtrdXZnbPfVBbukAbW4OGnUhNrA7SRAAfv2BSGk16PI0nBOr7qF2mItmBnjgEwk+DTv8Zr7w5qp7vleC6dIsZFNJoa6ZndrE/f7KO1CYruLXr5gwEkIyGfJ9NwyIagvHHMszzyHiSZIA850fWtbqtythpAliJ2jF35M5pNS+YTkRB+T6L/c6m00ymN3q9lT1rB03YywxrLreRSFZOSrbwWfg34EJbHfbFXpCSVYdJRfiVdvHnewN0r5fUlPtR9stQHyuqewzdkyb5jTTw02D2cUfL57vlPStBj7SEi3uOWvLrsiDnnCIxRMYJ2UA2ktDKHk+zWnsDmaeleSzonv2CHW42yXYPCvWi88oE1DJNYLNkIjua7MxAnkNZbScNw01A6zbLsZ3y8G6eEYnxSTRfwjd8EP4kdiHNJftm7Z4iRU7HOVh79/lRWB+gd171s3d/mI9kte3MRy6V8MMEMCAnMboGpaooYwgAmwclI2XZCczNWXfhaWe0ZS5PmytD/GDpXzkX0oEgY9K/uYo5V77NdZbGAjmyi8cE2B2ogvyaN2XfIInrZPgEffJ4AB7kFA2mwesdLOCh0BLD9itmCve3A1FGR4+stO2ANUoiI3w3Tv2yQSg4bjeDlJ08lXaaFCLW2peEXMXjQUk7fmpb5MNuOUTW6BE=");
        newCsd.setPrivateKey("MIIFDjBABgkqhkiG9w0BBQ0wMzAbBgkqhkiG9w0BBQwwDgQIAgEAAoIBAQACAggAMBQGCCqGSIb3DQMHBAgwggS/AgEAMASCBMh4EHl7aNSCaMDA1VlRoXCZ5UUmqErAbucoZQObOaLUEm+I+QZ7Y8Giupo+F1XWkLvAsdk/uZlJcTfKLJyJbJwsQYbSpLOCLataZ4O5MVnnmMbfG//NKJn9kSMvJQZhSwAwoGLYDm1ESGezrvZabgFJnoQv8Si1nAhVGTk9FkFBesxRzq07dmZYwFCnFSX4xt2fDHs1PMpQbeq83aL/PzLCce3kxbYSB5kQlzGtUYayiYXcu0cVRu228VwBLCD+2wTDDoCmRXtPesgrLKUR4WWWb5N2AqAU1mNDC+UEYsENAerOFXWnmwrcTAu5qyZ7GsBMTpipW4Dbou2yqQ0lpA/aB06n1kz1aL6mNqGPaJ+OqoFuc8Ugdhadd+MmjHfFzoI20SZ3b2geCsUMNCsAd6oXMsZdWm8lzjqCGWHFeol0ik/xHMQvuQkkeCsQ28PBxdnUgf7ZGer+TN+2ZLd2kvTBOk6pIVgy5yC6cZ+o1Tloql9hYGa6rT3xcMbXlW+9e5jM2MWXZliVW3ZhaPjptJFDbIfWxJPjz4QvKyJk0zok4muv13Iiwj2bCyefUTRz6psqI4cGaYm9JpscKO2RCJN8UluYGbbWmYQU+Int6LtZj/lv8p6xnVjWxYI+rBPdtkpfFYRp+MJiXjgPw5B6UGuoruv7+vHjOLHOotRo+RdjZt7NqL9dAJnl1Qb2jfW6+d7NYQSI/bAwxO0sk4taQIT6Gsu/8kfZOPC2xk9rphGqCSS/4q3Os0MMjA1bcJLyoWLp13pqhK6bmiiHw0BBXH4fbEp4xjSbpPx4tHXzbdn8oDsHKZkWh3pPC2J/nVl0k/yF1KDVowVtMDXE47k6TGVcBoqe8PDXCG9+vjRpzIidqNo5qebaUZu6riWMWzldz8x3Z/jLWXuDiM7/Yscn0Z2GIlfoeyz+GwP2eTdOw9EUedHjEQuJY32bq8LICimJ4Ht+zMJKUyhwVQyAER8byzQBwTYmYP5U0wdsyIFitphw+/IH8+v08Ia1iBLPQAeAvRfTTIFLCs8foyUrj5Zv2B/wTYIZy6ioUM+qADeXyo45uBLLqkN90Rf6kiTqDld78NxwsfyR5MxtJLVDFkmf2IMMJHTqSfhbi+7QJaC11OOUJTD0v9wo0X/oO5GvZhe0ZaGHnm9zqTopALuFEAxcaQlc4R81wjC4wrIrqWnbcl2dxiBtD73KW+wcC9ymsLf4I8BEmiN25lx/OUc1IHNyXZJYSFkEfaxCEZWKcnbiyf5sqFSSlEqZLc4lUPJFAoP6s1FHVcyO0odWqdadhRZLZC9RCzQgPlMRtji/OXy5phh7diOBZv5UYp5nb+MZ2NAB/eFXm2JLguxjvEstuvTDmZDUb6Uqv++RdhO5gvKf/AcwU38ifaHQ9uvRuDocYwVxZS2nr9rOwZ8nAh+P2o4e0tEXjxFKQGhxXYkn75H3hhfnFYjik/2qunHBBZfcdG148MaNP6DjX33M238T9Zw/GyGx00JMogr2pdP4JAErv9a5yt4YR41KGf8guSOUbOXVARw6+ybh7+meb7w4BeTlj3aZkv8tVGdfIt3lrwVnlbzhLjeQY6PplKp3/a5Kr5yM0T4wJoKQQ6v3vSNmrhpbuAtKxpMILe8CQoo=");
        newCsd.setPrivateKeyPassword("12345678a");
        newCsd.setRfc("EKU9003173C9");
        return facturama.Csd().Create(newCsd);

    }

    //Test CFDI 3.3
    private static void sampleCfdi(FacturamaApiMultiemisor facturama) throws IOException, FacturamaException, Exception {

        System.out.println("----- Inicio del ejemplo de CFDI -----");

        // Se obtiene la moneda con el valor "MXN"
        List<Currency> lstCurrencies = facturama.Catalogs().Currencies();
        Currency currency = lstCurrencies.stream().
                filter(p -> p.getValue().equals("MXN")).findFirst().get();

        // -------- Creacion del cfdi en su forma general (sin items / productos) asociados --------
        com.Facturama.sdk_java.Models.Request.CfdiLite cfdi = createCfdi(facturama, currency);

        // -------- Agregar los items que lleva el cfdi ( para este ejemplo, se agregan con datos aleatorios) --------        
        cfdi = addItemsToCfdi(facturama, cfdi);

        // Se obtiene la factura recien creada
        com.Facturama.sdk_java.Models.Response.Cfdi cfdiCreated = facturama.Cfdis().Create(cfdi);
        //com.Facturama.sdk_java.Models.Response.Cfdi cfdiCreated = facturama.Cfdis().Create3(cfdi); // CFDI 4.0 Disponible hasta el 30 de Junio 2022

        System.out.println("Se creó exitosamente el cfdi con el folio fiscal: " + cfdiCreated.getComplement().getTaxStamp().getUuid()); // CFDI 3.3
        //System.out.println( "Se creó exitosamente el cfdi con el folio fiscal: " +  cfdiCreated.getComplement().getTaxStamp().getUuid() ); // cfdi 4.0 test

        // Descarga de los archivos de la factura
        //String filePath = "factura"+cfdiCreated.getComplement().getTaxStamp().getUuid();
        //facturama.Cfdis().SaveXml(filePath+".xml", cfdiCreated.getId());
        // Se elmina la factura recien creada
        CancelationStatus response = facturama.Cfdis().Remove(cfdiCreated.getId(), "02", "d8e34bab-5bd4-4788-bde2-1428dc469e10");

        System.out.println(response.getStatus());

        String strCanceled = "canceled";
        String strPending = "canceled";
        if (strCanceled.equals(response.getStatus())) {
            System.out.println("Se ha cancelado exitosamente el cfdi con el folio fiscal: " + cfdiCreated.getComplement().getTaxStamp().getUuid());
        } else if (strPending.equals(response.getStatus())) {
            System.out.println("La factura está en proceso de cancelación, pueden pasar hasta 72 horas para que se considere cancelada.");
        } else {
            System.out.println("Algo ha pasado, que el CFDI no se ha podido cancelar. Revisa el mensaje: " + response.getMessage());
        }

        //El correo que se ingrese debe existir 
        // Consulta de cfdis mediante palabra clave o rfc
        //System.out.println( "Consulta de RFCs mediante RFC" );  
        //List<CfdiSearchResult> lstCfdiFilteredByKeyword = facturama.Cfdis().List("Expresion en Software");
        //List<CfdiSearchResult> lstCfdiFilteredByRfc = facturama.Cfdis().ListFilterByRfc("ESO1202108R2");                
        //System.out.println("Se obtiene la lista de facturas: " + lstCfdiFilteredByKeyword.size());
        //System.out.println("Se obtiene la lista de facturas por RFC: " + lstCfdiFilteredByRfc.size());
        System.out.println("----- Fin del ejemplo de CFDI -----");

    }

    //Test CFDI 4.0 Multiemisor
    private static void sampleCfdi40(FacturamaApiMultiemisor facturama) throws IOException, FacturamaException, Exception {

        System.out.println("----- Inicio del ejemplo de CFDI 4.0 -----");

        // Se obtiene la moneda con el valor "MXN"
        List<Currency> lstCurrencies = facturama.Catalogs().Currencies();
        Currency currency = lstCurrencies.stream().
                filter(p -> p.getValue().equals("MXN")).findFirst().get();

        // -------- Creacion del cfdi en su forma general (sin items / productos) asociados --------
        com.Facturama.sdk_java.Models.Request.CfdiLite cfdi = createCfdi40(facturama, currency);

        // -------- Agregar los items que lleva el cfdi ( para este ejemplo, se agregan con datos aleatorios) --------        
        //cfdi = addItemsToCfdi(facturama, cfdi);
        cfdi = addStaticItemsToCfdi(facturama, cfdi);

        // Se obtiene la factura recien creada
        com.Facturama.sdk_java.Models.Response.Cfdi cfdiCreated = facturama.Cfdis().Create3(cfdi);

        System.out.println("Se creó exitosamente el cfdi con el folio fiscal: " + cfdiCreated.getComplement().getTaxStamp().getUuid());

        // Descarga de los archivos de la factura
        //String filePath = "factura"+cfdiCreated.getComplement().getTaxStamp().getUuid();
        //facturama.Cfdis().SaveXml(filePath+".xml", cfdiCreated.getId());
        // Se elmina la factura recien creada
        CancelationStatus response = facturama.Cfdis().Remove(cfdiCreated.getId(), "02", "");
        System.out.println(response.getStatus());

        String strCanceled = "canceled";
        String strPending = "canceled";
        if (strCanceled.equals(response.getStatus())) {
            System.out.println("Se ha cancelado exitosamente el cfdi con el folio fiscal: " + cfdiCreated.getComplement().getTaxStamp().getUuid());
        } else if (strPending.equals(response.getStatus())) {
            System.out.println("La factura está en proceso de cancelación, pueden pasar hasta 72 horas para que se considere cancelada.");
        } else {
            System.out.println("Algo ha pasado, que el CFDI no se ha podido cancelar. Revisa el mensaje: " + response.getMessage());
        }

        //El correo que se ingrese debe existir 
        // Consulta de cfdis mediante palabra clave o rfc
        //System.out.println( "Consulta de RFCs mediante RFC" );  
        //List<CfdiSearchResult> lstCfdiFilteredByKeyword = facturama.Cfdis().List("Expresion en Software");
        //List<CfdiSearchResult> lstCfdiFilteredByRfc = facturama.Cfdis().ListFilterByRfc("ESO1202108R2");                
        //System.out.println("Se obtiene la lista de facturas: " + lstCfdiFilteredByKeyword.size());
        //System.out.println("Se obtiene la lista de facturas por RFC: " + lstCfdiFilteredByRfc.size());
        System.out.println("----- Fin del ejemplo de CFDI -----");

    }

    private static com.Facturama.sdk_java.Models.Request.CfdiLite createCfdi(FacturamaApiMultiemisor facturama, Currency currency)
            throws IOException, FacturamaException, Exception {

        com.Facturama.sdk_java.Models.Request.CfdiLite cfdi = new com.Facturama.sdk_java.Models.Request.CfdiLite();

        // Método de pago       
        Catalog paymentMethod = facturama.Catalogs().PaymentMethods().stream().
                filter(p -> p.getName().equals("Pago en una sola exhibición")).findFirst().get();

        // Forma de pago
        Catalog paymentForm = facturama.Catalogs().PaymentForms().stream().
                filter(p -> p.getName().equals("Efectivo")).findFirst().get();

        // Cliente (se toma como cliente el "cliente generico", aquel que tiene el RFC genérico),
        // (como los clientes son exclusivos para cada usuario, se debe previamente dar de alta este cliente)
        // Lugar de expedición
        cfdi.setFolio("10");
        cfdi.setNameId(facturama.Catalogs().NameIds().get(1).getValue());
        cfdi.setCfdiType(CfdiType.Ingreso.getValue());
        cfdi.setPaymentForm(paymentForm.getValue());
        cfdi.setPaymentMethod(paymentMethod.getValue());
        cfdi.setCurrency(currency.getValue());

        // logo - Se especifica como una URL
        cfdi.setLogoUrl("http://URL_de_tu_logo_ejemplo.ejemplo");

        // Datos no fiscales (se muestran en el PDF)
        cfdi.setObservations("Este es un ejemplo de observaciones");
        cfdi.setOrderNumber("123321");
        cfdi.setPaymentBankName("BBVA");
        cfdi.setPaymentAccountNumber("6789");
        cfdi.setPaymentConditions("Condiciones");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        cfdi.setDate(dateFormat.format(date));
        cfdi.setExpeditionPlace("78140");

        Receiver receiver = new Receiver();
        receiver.setCfdiUse("G03");
        receiver.setName("UNIVERSIDAD ROBOTICA ESPAÑOLA");
        receiver.setRfc("URE180429TM6");
        //receiver.setFiscalRegime("601"); // Valores para CFDI 4.0
        //receiver.setTaxZipCode("65000"); // Valores para CFDI 4.0

        Issuer issuer = new Issuer();
        issuer.setFiscalRegime(facturama.Catalogs().FiscalRegimens().get(0).getValue());
        issuer.setName("ESCUELA KEMPER URGATE");
        issuer.setRfc("EKU9003173C9");
        cfdi.setIssuer(issuer);
        cfdi.setReceiver(receiver);

        return cfdi;

    }

    //Llenado del CFDI 4.0
    private static com.Facturama.sdk_java.Models.Request.CfdiLite createCfdi40(FacturamaApiMultiemisor facturama, Currency currency)
            throws IOException, FacturamaException, Exception {
        com.Facturama.sdk_java.Models.Request.CfdiLite cfdi = new com.Facturama.sdk_java.Models.Request.CfdiLite();

        cfdi.setNameId("1");
        cfdi.setFolio("100");
        cfdi.setSerie("FAC");
        cfdi.setCfdiType("I");
        cfdi.setPaymentForm("03");
        cfdi.setPaymentMethod("PUE");
        cfdi.setOrderNumber("TEST-001");
        cfdi.setCurrency("MXN");
        cfdi.setExpeditionPlace("78000");
        cfdi.setExportation("01");

        // logo - Se especifica como una URL
        cfdi.setLogoUrl("https://www.ejemplos.co/wp-content/uploads/2015/11/Logo-Chanel.jpg");

        // Datos no fiscales (se muestran en el PDF)
        cfdi.setObservations("Este es un ejemplo de observaciones");
        cfdi.setPaymentBankName("BBVA");
        cfdi.setPaymentAccountNumber("6789");
        cfdi.setPaymentConditions("Condiciones");

        //cfdi = addIneComplement(facturama, cfdi);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        cfdi.setDate(dateFormat.format(date));

        //Nodo Informacion Global
//            GlobalInformation globalinformation=new GlobalInformation();
//            globalinformation.setPeriodicity("02");
//            globalinformation.setMonths("04");
//            globalinformation.setYear("2022");
//            cfdi.setGlobalInformation(globalinformation);
//            
        // Receptor de la factura
        Receiver receiver = new Receiver();
        receiver.setRfc("ZUÑ920208KL4");
        receiver.setName("ZAPATERIA URTADO ÑERI");
        receiver.setCfdiUse("G03");
        receiver.setTaxZipCode("34541");
        receiver.setFiscalRegime("601");

        //Emisor de la factura
        Issuer issuer = new Issuer();
        issuer.setFiscalRegime("601");
        issuer.setName("ESCUELA KEMPER URGATE");
        issuer.setRfc("EKU9003173C9");

        cfdi.setIssuer(issuer);
        cfdi.setReceiver(receiver);

        return cfdi;

    }

    private static com.Facturama.sdk_java.Models.Request.CfdiLite addItemsToCfdi(FacturamaApiMultiemisor facturama,
            com.Facturama.sdk_java.Models.Request.CfdiLite cfdi) throws IOException, FacturamaException, Exception {

        Double price = 100.00;
        Double quantity = 2.00;
        Double discount = 10.00;
        Currency currency = facturama.Catalogs().Currency("MXN");
        int decimals = (int) currency.getDecimals();
        Double numberOfDecimals = Math.pow(10, decimals);

        Double subtotal = Math.round((price * quantity) * numberOfDecimals) / numberOfDecimals;
        List<Item> lstItems = new ArrayList<>();
        Item item = new Item();
        //item.setProductCode(facturama.Catalogs().ProductsOrServices("desarrollo").get(0).getValue());
        item.setProductCode("84111506");
        item.setUnitCode(facturama.Catalogs().Units("pieza").get(1).getValue());
        item.setUnit("Libra");
        item.setDescription("Descripción del Producto");
        item.setIdentificationNumber("010101-56");
        item.setQuantity(quantity);
        item.setDiscount(Math.round(discount * numberOfDecimals) / numberOfDecimals);
        item.setUnitPrice(Math.round(price * numberOfDecimals) / numberOfDecimals);
        item.setSubtotal(subtotal);
        item.setTaxObject("02");

        //A cuenta de terceros
        /*
            ThirdPartyAccount thirdPartyAccount= new ThirdPartyAccount(); 
            thirdPartyAccount.setRfc("CACX7605101P8");
            thirdPartyAccount.setName("XOCHILT CASAS CHAVEZ");
            thirdPartyAccount.setFiscalRegime("616");
            thirdPartyAccount.setTaxZipCode("10740");
            item.setThirdPartyAccount(thirdPartyAccount);
            item.setCuentaPredial("12345");
         */
        //Número Pedimento
        /*
            ArrayList<String> numerosPedimento=new ArrayList<>();
            numerosPedimento.add("21  47  3807  8003832");
            numerosPedimento.add("21  47  3807  8003832");
            numerosPedimento.add("21  47  3807  8003832");
            item.setNumerosPedimento(numerosPedimento);
         */
        lstItems.add(item);

        item = addTaxesToItem(item, numberOfDecimals);

        cfdi.setItems(lstItems);

        return cfdi;
    }

    private static Item addTaxesToItem(Item item, Double numberOfDecimals) {

        List<Tax> lstTaxes = new ArrayList<>();              // Impuestos del item (del cfdi)

        Tax tax = new Tax();

        tax.setName("IVA");
        tax.setIsQuota(false);
        tax.setIsRetention(false);

        tax.setRate(0.160000d);
        tax.setBase(Math.round(item.getSubtotal() * numberOfDecimals) / numberOfDecimals);
        tax.setTotal(Math.round((/*cambie el baseAmount*/tax.getBase() * tax.getRate()) * numberOfDecimals) / numberOfDecimals);

        lstTaxes.add(tax);

        Double retentionsAmount = 0D;
        Double transfersAmount = 0D;

        // Asignación de los impuestos, en caso de que no se tengan, el campo va nulo
        if (!lstTaxes.isEmpty()) {
            item.setTaxes(lstTaxes);

            retentionsAmount = item.getTaxes().stream().filter(o -> o.getIsRetention()).mapToDouble(o -> o.getTotal()).sum();
            transfersAmount = item.getTaxes().stream().filter(o -> !o.getIsRetention()).mapToDouble(o -> o.getTotal()).sum();
        }

        // Calculo del subtotal
        item.setTotal(item.getSubtotal() - item.getDiscount() + transfersAmount - retentionsAmount);
        //item.setObjetoImp("02");// Nuevo elemento CFDI 4.0
        return item;

    }

    /*
     * Ejemplo de creación de un CFDI "complemento de pago"
     * Referencia: https://apisandbox.facturama.mx/guias/api-web/cfdi/complemento-pago
     * 
     * En virtud de que el complemento de pago, requiere ser asociado a un CFDI con el campo "PaymentMethod" = "PPD"
     * En este ejemplo se incluye la creacón de este CFDI, para posteriormente realizar el  "Complemento de pago" = "PUE"     
     */
    
    private static void samplePaymentComplement(FacturamaApiMultiemisor facturama)
            throws IOException, FacturamaException, Exception {

        System.out.println("----- Inicio del ejemplo samplePaymentComplement -----");

        System.out.println("Creación del CFDI Inicial (PPD)");
        // Cfdi Incial (debe ser "PPD")
        // -------- Creacion del cfdi en su forma general (sin items / productos) asociados --------
        com.Facturama.sdk_java.Models.Request.CfdiLite cfdi = createModelCfdiGeneral(facturama);

        // -------- Agregar los items que lleva el cfdi ( para este ejemplo, se agregan con datos aleatorios) --------        
        cfdi = addItemsToCfdi(facturama, cfdi);
        
        Random randomNumbers = new Random();
        int RandomFolio = randomNumbers.nextInt(999) + 1;
        String nFolio = "MULTI" + RandomFolio;
        cfdi.setFolio(nFolio);
        
        cfdi.setPaymentMethod("PPD"); 
        cfdi.setPaymentForm("99");// El método de pago del documento inicial debe ser "PPD"

        // logo - Se especifica como una URL
        cfdi.setLogoUrl("https://www.ejemplos.co/wp-content/uploads/2015/11/Logo-Chanel.jpg");

        // Se manda timbrar mediante Facturama
        com.Facturama.sdk_java.Models.Response.Cfdi cfdiInicial = facturama.Cfdis().Create3(cfdi);

        System.out.println("Se creó exitosamente el cfdi Inicial (PPD) con el folio fiscal: " + cfdiInicial.getComplement().getTaxStamp().getUuid());

        // Descarga de los archivos del documento inicial
//        String filePath = "factura" + cfdiInicial.getComplement().getTaxStamp().getUuid();
//        facturama.Cfdis().SavePdf(filePath + ".pdf", cfdiInicial.getId());
//        facturama.Cfdis().SaveXml(filePath + ".xml", cfdiInicial.getId());

        // Complemento de pago (debe ser "PUE")        
        // Y no lleva "Items" solo especifica el "Complemento"
        System.out.println("Creación del complemento de Pago ");

        com.Facturama.sdk_java.Models.Request.CfdiLite paymentComplementModel = createModelCfdiPaymentComplement(facturama, cfdiInicial);

        // Se manda timbrar el complemento de pago mediante Facturama
        com.Facturama.sdk_java.Models.Response.Cfdi paymentComplement = facturama.Cfdis().Create3(paymentComplementModel);

        System.out.println("Se creó exitosamente el complemento de pago con el folio fiscal: " + paymentComplement.getComplement().getTaxStamp().getUuid());

        // Descarga de los archivos del documento inicial
//        String filePathPayment = "factura" + paymentComplement.getComplement().getTaxStamp().getUuid();
//        facturama.Cfdis().SavePdf(filePath + ".pdf", paymentComplement.getId());
//        facturama.Cfdis().SaveXml(filePath + ".xml", paymentComplement.getId());

        // Posibilidad de mandar  los cfdis por coreo ( el cfdiInical y complemento de pago)
//        System.out.println(facturama.Cfdis().SendEmail("ejemplo@ejemplo.mx", com.Facturama.sdk_java.Services.Multiemisor.CfdiService.InvoiceType.IssuedLite, cfdiInicial.getId()));
//        System.out.println(facturama.Cfdis().SendEmail("ejemplo@ejemplo.mx", com.Facturama.sdk_java.Services.Multiemisor.CfdiService.InvoiceType.IssuedLite, paymentComplement.getId()));

        System.out.println("----- Fin del ejemplo de samplePaymentComplement -----");

    }

    /*
     * Llenado del modelo de CFDI, de una forma general
     * - Se especifica: la moneda, método de pago, forma de pago, cliente, y lugar de expedición     
     */
    private static com.Facturama.sdk_java.Models.Request.CfdiLite createModelCfdiGeneral(FacturamaApiMultiemisor facturama)
            throws IOException, FacturamaException, Exception {

        System.out.println("createModelCfdiGeneral");

        Currency currency = facturama.Catalogs().Currency("MXN");

        com.Facturama.sdk_java.Models.Request.CfdiLite cfdi = new com.Facturama.sdk_java.Models.Request.CfdiLite();
        // Lista del catálogo de nombres en el PDF
        NameCfdi nameForPdf = facturama.Catalogs().NameIds().get(0); // Nombre en el pdf: "Factura"

        // Método de pago                             
        Catalog paymentMethod = facturama.Catalogs().PaymentMethod("PUE");

        // Forma de pago
        Catalog paymentForm = facturama.Catalogs().PaymentForms().stream().
                filter(p -> p.getName().equals("Efectivo")).findFirst().get();

        cfdi.setNameId(nameForPdf.getValue());
        cfdi.setCfdiType(CfdiType.Ingreso.getValue());
        cfdi.setPaymentForm(paymentForm.getValue());
        cfdi.setPaymentMethod(paymentMethod.getValue());
        cfdi.setCurrency(currency.getValue());
        cfdi.setExpeditionPlace("78180");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        cfdi.setDate(dateFormat.format(date));

        Receiver receiver = new Receiver();
        receiver.setRfc("ZUÑ920208KL4");
        receiver.setName("ZAPATERIA URTADO ÑERI");
        receiver.setCfdiUse("CP01");
        receiver.setTaxZipCode("34541");
        receiver.setFiscalRegime("601");
        cfdi.setReceiver(receiver);

        Issuer issuer = new Issuer();
        issuer.setFiscalRegime("601");
        issuer.setName("ESCUELA KEMPER URGATE");
        issuer.setRfc("EKU9003173C9");
        
        cfdi.setIssuer(issuer);

        return cfdi;

    }

    /*
     * Modelo "Complemento de pago"
     * - Se especifica: la moneda, método de pago, forma de pago, cliente, y lugar de expedición     
     */
    private static com.Facturama.sdk_java.Models.Request.CfdiLite
            createModelCfdiPaymentComplement(FacturamaApiMultiemisor facturama, com.Facturama.sdk_java.Models.Response.Cfdi cfdiInicial)
            throws IOException, FacturamaException, Exception {

        System.out.println("createModelCfdiPaymentComplement");

        com.Facturama.sdk_java.Models.Request.CfdiLite cfdi = new com.Facturama.sdk_java.Models.Request.CfdiLite();

        // Lista del catálogo de nombres en el PDF
        NameCfdi nameForPdf = facturama.Catalogs().NameIds().get(13); // Nombre en el pdf: "Complemento de pago"

        // Forma de pago
        Catalog paymentForm = facturama.Catalogs().PaymentForms().stream().
                filter(p -> p.getName().equals("Efectivo")).findFirst().get();

        cfdi.setNameId(nameForPdf.getValue());
        cfdi.setCfdiType(CfdiType.Pago.getValue()); // "P"  (El comprobante es del tipo "Pago")
        
        Random randomNumbers = new Random();
        int RandomFolio = randomNumbers.nextInt(999) + 1;
        String nFolio = "COMPMUL" + RandomFolio;
        cfdi.setFolio(nFolio);

        // logo - Se especifica como una URL
        cfdi.setLogoUrl("https://www.ejemplos.co/wp-content/uploads/2015/11/Logo-Chanel.jpg");

        Receiver receiver = new Receiver();
        receiver.setRfc("ZUÑ920208KL4");
        receiver.setName("ZAPATERIA URTADO ÑERI");
        receiver.setCfdiUse("CP01");
        receiver.setTaxZipCode("34541");
        receiver.setFiscalRegime("601");
        cfdi.setReceiver(receiver);

        Issuer issuer = new Issuer();
        issuer.setFiscalRegime("601");
        issuer.setName("ESCUELA KEMPER URGATE");
        issuer.setRfc("EKU9003173C9");
        cfdi.setIssuer(issuer);

        cfdi.setExpeditionPlace("78000");

        // Fecha y hora de expecidión del comprobante
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date cfdiDate = new Date();
        cfdi.setDate(dateFormat.format(cfdiDate));

        // Complemento de pago ---
        Complements complement = new Complements();

        // Pueden representarse más de un pago en un solo CFDI
        List<Payment> lstPagos = new ArrayList();
        Payment pago = new Payment();

        // Fecha y hora en que se registró el pago en el formato: "yyyy-MM-ddTHH:mm:ss" 
        // (la fecha del pago debe ser menor que la fecha en que se emite el CFDI)
        // Para este ejemplo, se considera que  el pago se realizó hace una hora            
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(cfdiDate);
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        pago.setDate(dateFormat.format(calendar.getTime()));

        // Selección de la moneda del catálogo
        // La Moneda, puede ser diferente a la del documento inicial
        // (En el caso de que sea diferente, se debe colocar el tipo de cambio)
        List<Currency> lstCurrencies = facturama.Catalogs().Currencies();
        Currency currency = lstCurrencies.stream().
                filter(p -> p.getValue().equals("MXN")).findFirst().get();
        pago.setCurrency(currency.getValue());              // Moneda en que se realiza el pago

        // Monto del pago
        // Este monto se puede distribuir entre los documentos relacionados al pago            
        pago.setAmount(116.00);
        pago.setPaymentForm(paymentForm.getValue());

        // Documentos relacionados con el pago
        // En este ejemplo, los datos se obtiene el cfdiInicial, pero puedes colocar solo los datos
        // aun sin tener el "Objeto" del cfdi Inicial, ya que los valores son del tipo "String"
        List<RelatedDocument> lstRelatedDocuments = new ArrayList();
        RelatedDocument relatedDocument = new RelatedDocument();
        relatedDocument.setUuid(cfdiInicial.getComplement().getTaxStamp().getUuid()); // "27568D31-E579-442F-BA77-798CBF30BD7D"
        relatedDocument.setSerie(cfdiInicial.getSerie()); // "EA"
        relatedDocument.setFolio(cfdiInicial.getFolio()); // 34853
        relatedDocument.setCurrency(currency.getValue());
        relatedDocument.setPaymentMethod("PPD");            // Metodo de pago del CFDI Inicial
        relatedDocument.setPartialityNumber(1);
        relatedDocument.setPreviousBalanceAmount(116.00);
        relatedDocument.setAmountPaid(116.00);
        relatedDocument.setImpSaldoInsoluto(0.00);
        relatedDocument.setTaxObject("02");
        
        List<Tax> lstTaxes = new ArrayList<Tax>();

        Tax tax = new Tax();

        tax.setBase(100.00);
        tax.setRate(0.16);
        tax.setName("IVA");
        tax.setIsRetention(false);
        tax.setIsQuota(false);
        tax.setTotal(16.00);
        lstTaxes.add(tax);

        relatedDocument.setTaxes(lstTaxes);

        lstRelatedDocuments.add(relatedDocument);

        pago.setRelatedDocument(lstRelatedDocuments);

        lstPagos.add(pago);

        complement.setPayments(lstPagos);

        cfdi.setComplements(complement);

        return cfdi;

    }

    //Add Complement    
    private static com.Facturama.sdk_java.Models.Request.CfdiLite addIneComplement(FacturamaApiMultiemisor facturama,
            com.Facturama.sdk_java.Models.Request.CfdiLite cfdi) throws IOException, FacturamaException, Exception {

        Complements complement = new Complements();
        Ine ine = new Ine();

//        ine.setVersion("1.1");
//        ine.setTipoProceso("Ordinario");
//        ine.setTipoComite("EjecutivoNacional");
//        ine.setIdContabilidad("123456");
//        ine.setTipoComiteSpecified(true);
        ine.setVersion("1.1");
        ine.setTipoProceso("Precampaña");
        List<IneEntidad> lstentidad = new ArrayList();
        IneEntidad entidad = new IneEntidad();

        entidad.setAmbito("Federal");
        entidad.setClaveEntidad("AGU");

        List<IdContabilidad> lstContabilidad = new ArrayList<>();

        IdContabilidad idContabilidad = new IdContabilidad();
        idContabilidad.setIdContabilidad("123456");
        lstContabilidad.add(idContabilidad);

        entidad.setContabilidad(lstContabilidad);
        lstentidad.add(entidad);
        ine.setEntidad(lstentidad);

        complement.setIne(ine);
        cfdi.setComplements(complement);

        return cfdi;
    }

    private static void TestCancel(FacturamaApiMultiemisor facturama) throws IOException, FacturamaException, Exception {
        System.out.println("----- Inicio del ejemplo Test Cancelación-----");

        String Cfdi_Id = "";
        // Se elmina la factura recien creada
        CancelationStatus response = facturama.Cfdis().Remove(Cfdi_Id, "02", null);

        System.out.println(response.getStatus());

        String strCanceled = "canceled";
        String strPending = "canceled";
        if (strCanceled.equals(response.getStatus())) {
            System.out.println("Se ha cancelado exitosamente el cfdi con el folio fiscal: " + response.getUuid());
        } else if (strPending.equals(response.getStatus())) {
            System.out.println("La factura está en proceso de cancelación, pueden pasar hasta 72 horas para que se considere cancelada.");
        } else {
            System.out.println("Algo ha pasado, que el CFDI no se ha podido cancelar. Revisa el mensaje: " + response.getMessage());
        }

        System.out.println("----- Fin del ejemplo Test Cancelación -----");
    }

    private static com.Facturama.sdk_java.Models.Request.CfdiLite addStaticItemsToCfdi(FacturamaApiMultiemisor facturama, com.Facturama.sdk_java.Models.Request.CfdiLite cfdi) throws IOException, FacturamaException, Exception {

        // Lista de Items en el cfdi (los articulos a facturar)
        List<Item> lstItems = new ArrayList<>();

        // Llenado del item (que va en el cfdi)
        Item item = new Item();

        Currency currency = facturama.Catalogs().Currency("MXN");
        int decimals = (int) currency.getDecimals();
        Double numberOfDecimals = Math.pow(10, decimals);

        item.setUnit("Servicio");
        item.setUnitCode("E48");
        item.setIdentificationNumber("WEB003");
        item.setProductCode("10101504");
        item.setDescription("Estudios de laboratorio");
        item.setQuantity(2.0);
        item.setDiscount(0.0);
        item.setUnitPrice(50.0);
        Double subTotal = Math.round((item.getUnitPrice() * item.getQuantity()) * numberOfDecimals) / numberOfDecimals;
        item.setSubtotal(subTotal);
        item.setTaxObject("02");

        List<Tax> lstTaxes = new ArrayList<>();
        Tax tax = new Tax();

        tax.setBase(item.getSubtotal());
        tax.setRate(0.16);
        tax.setName("IVA");
        tax.setIsRetention(false);
        tax.setIsQuota(false);
        tax.setTotal(Math.round(tax.getBase() * tax.getRate() * numberOfDecimals) / numberOfDecimals);
        lstTaxes.add(tax);
        item.setTaxes(lstTaxes);
        lstItems.add(item);

        item.setTotal(Math.round((tax.getTotal() + item.getSubtotal()) * numberOfDecimals) / numberOfDecimals);
        cfdi.setItems(lstItems);

        return cfdi;
    }

}
