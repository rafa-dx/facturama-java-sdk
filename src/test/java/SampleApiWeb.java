
import java.io.IOException;
import com.Facturama.sdk_java.Container.FacturamaApi;
import com.Facturama.sdk_java.Services.*;
import com.Facturama.sdk_java.Models.*;
import com.Facturama.sdk_java.Models.Request.ProductTax;
import com.Facturama.sdk_java.Models.Request.CfdiType;
import com.Facturama.sdk_java.Models.Request.Item;
import com.Facturama.sdk_java.Models.Request.Receiver;
import com.Facturama.sdk_java.Models.Request.GlobalInformation;
import com.Facturama.sdk_java.Models.Request.ThirdPartyAccount;
import com.Facturama.sdk_java.Models.Request.Tax;
import com.Facturama.sdk_java.Models.Response.Catalogs.*;
import com.Facturama.sdk_java.Models.Response.Catalogs.Cfdi.*;
import com.Facturama.sdk_java.Models.Response.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import com.Facturama.sdk_java.Models.Exception.FacturamaException;
import com.Facturama.sdk_java.Models.Request.Complements.Complements;
import com.Facturama.sdk_java.Models.Request.Complements.INE.IdContabilidad;
import com.Facturama.sdk_java.Models.Request.Payment;
import com.Facturama.sdk_java.Models.Request.RelatedDocument;
import com.Facturama.sdk_java.Models.Request.Complements.INE.Ine;
import com.Facturama.sdk_java.Models.Request.Complements.INE.IneEntidad;

import java.util.Calendar;
import java.util.Map;

/*
 * Soporte API
 * @author Facturama
 * facturamasoporte@freshbooks.com
 */
public class SampleApiWeb {

    public static void principal() {
        System.out.println("Ejemplos de consumo de la FacturamaAPI Web, con el usuario de 'pruebas'");

        try {
            // Creación de la instancia
            FacturamaApi facturama = createApiInstance();

            // Prueba de la funcionalidad básica del servicio de clientes
            //sampleClients(facturama);
            
            //Prueba de funcionalidad de crear un producto
            //sampleProducts(facturama);
            
            // Ejemplo de la funcionalidad básica del servicio de CFDI (crear factura)
            //sampleCfdi40(facturama);    // Test CFDI 4.0 
            //sampleCfdiGlobal(facturama);    // Test CFDI 4.0 Factura Global

            //Prueba de funcionalidad de crear un producto             
            //sampleList(facturama);
            
            // Ejemplo de la creación de un complemento de pago
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
    * Creación de una instancia del SDK con los datos del usuario de sdkpruebas
    * Remplaza 'user' y 'password' por los de tu cuenta
    * isDevMode = true : ambiente de pruebas ( no se consumen folios, las facturas realzadas son apócrifas)
    * isDevMode = false : ambiente de producción ( SI se consumen folios, las facturas realzadas son TIMBRADAS por un "PAC" )
     */
    private static FacturamaApi createApiInstance() {
        String user = "sdkpruebas";
        String password = "pruebas2022";
        Boolean isDevMode = true;

        return new FacturamaApi(user, password, isDevMode);
    }

    /*
     * Ejemplos de funcionalidad basica de los servicios de "Clientes" 
     * @param facturama Instancia de la api de facturama
     */
    private static void sampleClients(FacturamaApi facturama) throws IOException, FacturamaException, Exception {
        List<Client> lstClients = facturama.Clients().List();
        Integer clientsBefore = lstClients.size();

        // Creacion de cliente
        Client newClient = sampleClientsCreate(facturama);

        // El ciente obtenido (clientRetrived) seria el mismo que (newClientSaved)         
        String clientRetrivedId = newClient.getId();
        Client clientRetrived = facturama.Clients().Retrieve(clientRetrivedId);

        // Se modifica el RFC
        clientRetrived.setRfc("URE180429TM6");

        // Edicion
        facturama.Clients().Update(clientRetrived, clientRetrived.getId());
        System.out.println("Cliente Editado");

        // Eliminación
        facturama.Clients().Remove(clientRetrivedId);

        // Comparativa de la cantidad de clientes al inicio y final de la prueba,
        // para confirmar que se ha eliminado
        List<Client> lstClientsAfter = facturama.Clients().List();
        Integer clientsAfter = lstClientsAfter.size();

        if (Objects.equals(clientsBefore, clientsAfter)) {
            System.out.println("Creación y eliminacion de cliente correcta!");
        } else {
            System.out.println("Creación y eliminacion de cliente erronea!");
        }

        System.out.println("Fin del ejemplo de clientes");

    }

    private static Client sampleClientsCreate(FacturamaApi facturama) throws IOException, FacturamaException, Exception {
        Client newClient = new Client();

        Address clientAddress = new Address();

        clientAddress.setCountry("MEXICO");
        clientAddress.setExteriorNumber("1230");
        clientAddress.setInteriorNumber("B");
        clientAddress.setLocality("San Luis");
        clientAddress.setMunicipality("San Luis Potosí");
        clientAddress.setNeighborhood("Lomas 4ta");
        clientAddress.setState("San Luis Potosí");
        clientAddress.setStreet("Calle de pruebas");
        clientAddress.setZipCode("86991");

        newClient.setAddress(clientAddress);

        newClient.setRfc("URE180429TM6");
        newClient.setName("UNIVERSIDAD ROBOTICA ESPAÑOLA");
        newClient.setCfdiUse("G03");
        newClient.setFiscalRegime("601");
        newClient.setTaxZipCode("86991");
        newClient.setEmail("soporte-api@ejemplo.com");

        return facturama.Clients().Create(newClient);
    }

    private static void sampleProducts(FacturamaApi facturama) throws IOException, FacturamaException, Exception {
        List<Product> lstProducts = facturama.Products().List();
        Integer ProductsBefore = lstProducts.size();

        // Creacion de Producto
        Product newProduct = sampleProductCreate(facturama);

        // El producto obtenido (ProductRetrived) seria el mismo que (newProductSaved)         
        String ProductRetrivedId = newProduct.getId();
        Product ProductRetrived = facturama.Products().Retrieve(ProductRetrivedId);

        // Se modifica el Precio
        ProductRetrived.setPrice(100.00);

        // Edicion
        facturama.Products().Update(ProductRetrived, ProductRetrived.getId());
        System.out.println("Producto Editado");

        // Eliminación
        facturama.Products().Remove(ProductRetrivedId);

        // Comparativa de la cantidad de Productos al inicio y final de la prueba,
        // para confirmar que se ha eliminado
        List<Product> lstProductsAfter = facturama.Products().List();
        Integer ProductsAfter = lstProductsAfter.size();

        if (Objects.equals(ProductsBefore, ProductsAfter)) {
            System.out.println("Creación y eliminacion de Producto correcta!");
        } else {
            System.out.println("Creación y eliminacion de Producto erronea!");
        }

        System.out.println("Fin del ejemplo de Productes");

    }

    private static Product sampleProductCreate(FacturamaApi facturama) throws IOException, FacturamaException, Exception {

        Unit unit = facturama.Catalogs().Units("servicio").get(0);
        ProductServices prod = facturama.Catalogs().ProductsOrServices("desarrollo").get(0);

        Product product = new Product();
        List<ProductTax> taxes = new ArrayList();

        product.setUnit("Servicio");
        product.setUnitCode(unit.getValue());
        product.setIdentificationNumber("WEB003");
        product.setName("Sitio Web CMS");
        product.setDescription("Desarrollo e implementación de sitio web empleando un CMS");
        product.setPrice(6500.00);
        product.setCodeProdServ(prod.getValue());
        product.setCuentaPredial("123");

        ProductTax iva = new ProductTax();
        iva.setName("IVA");
        iva.setRate(0.16);
        iva.setIsRetention(false);

        /*ProductTax isr = new ProductTax();
        isr.setName("ISR");
        isr.setTotal(0.19);
        isr.setIsRetention(true);*/
        taxes.add(iva);
        //taxes.add(isr);

        product.setTaxes(taxes);

        product = facturama.Products().Create(product);
        System.out.println("Se creo exitosamente un producto con el id: " + product.getId());

        return product;
    }

    private static void sampleList(FacturamaApi facturama) throws IOException, FacturamaException, Exception {
        //List<CfdiSearchResult> lstCfdiFilteredByKeyword = facturama.Cfdis().List("ORGANICOS"); 
        //System.out.println("Se obtiene la lista de facturas: " + lstCfdiFilteredByKeyword.toString());

        //List<CfdiSearchResult> lstCfdiFilteredByRfc = facturama.Cfdis().ListFilterByRfc("ESO1202108R2"); 
        //System.out.println("Se obtiene la lista de facturas por RFC: " + lstCfdiFilteredByRfc.size());
        //Opción 1
        //List<CfdiSearchResult> lstCfdiByOrderNumber = facturama.Cfdis().ListFilterByOrderNumber(CfdiService.InvoiceType.Issued, "050408");
        //System.out.println("Se obtiene la lista de facturas: " + lstCfdiByOrderNumber.get(0).getId());
        //Opción 2, prueba con parametros
        int FolioIni = -1;
        int FolioFin = -1;
        String Rfc = "OÑO120726RX3";
        String taxEntityName = null;
        String dateStart = null;
        String dateEnd = null;
        String idBranch = null;
        String serie = null;
        CfdiService.CfdiStatus status = CfdiService.CfdiStatus.all;
        CfdiService.InvoiceType type = CfdiService.InvoiceType.Issued;
        String OrderNumber = "050408";

        List<CfdiSearchResult> lstCfdiByOrderNumber2 = facturama.Cfdis().List(FolioIni, FolioFin, Rfc, taxEntityName, dateStart, dateEnd, idBranch, serie, status, type, OrderNumber);
        System.out.println("Se obtiene la lista de facturas: " + lstCfdiByOrderNumber2.get(0).getId());

    }

    //Test CFDI 4.0 Tipo Ingreso
    private static void sampleCfdi40(FacturamaApi facturama) throws IOException, FacturamaException, Exception {

        System.out.println("----- Inicio del ejemplo de CFDI 4.0 -----");

        // -------- Creacion del cfdi en su forma general (sin items / productos) asociados --------
        com.Facturama.sdk_java.Models.Request.Cfdi cfdi = createModelCfdiGeneral40(facturama);

        // -------- Agregar los items que lleva el cfdi ( para este ejemplo, se agregan con datos aleatorios) --------        
        //cfdi = addItemsToCfdi(facturama, cfdi);
        cfdi = addStaticItemsToCfdi(facturama, cfdi);
        //cfdi = addIneComplement(facturama, cfdi);

        // Se obtiene la factura recien creada
        com.Facturama.sdk_java.Models.Response.Cfdi cfdiCreated = facturama.Cfdis().Create3(cfdi);

        System.out.println("Se creo CFDI con el siguiente ID: " + cfdiCreated.getId());
        System.out.println("\nSe creó exitosamente el cfdi con el folio fiscal: " + cfdiCreated.getComplement().getTaxStamp().getUuid());

        // Descarga de los archivos de la factura
        //String filePath = "factura"+cfdiCreated.getComplement().getTaxStamp().getUuid();
        //facturama.Cfdis().SavePdf(filePath+".pdf", cfdiCreated.getId());
        //facturama.Cfdis().SaveXml(filePath+".xml", cfdiCreated.getId());
        // Se elmina la factura recien creada
        facturama.Cfdis().Remove(cfdiCreated.getId(), "issued", "03", null);
        System.out.println("Se elminó exitosamente el cfdi con el folio fiscal: " + cfdiCreated.getComplement().getTaxStamp().getUuid());

        //El correo que se ingrese debe existir 
        //System.out.println(facturama.Cfdis().SendEmail("example@.mx",CfdiService.InvoiceType.Issued, "7eo51BvzV-E16gBx3nnxfQ2"));
        // Consulta de cfdis mediante palabra clave o rfc
        //List<CfdiSearchResult> lstCfdiFilteredByKeyword = facturama.Cfdis().List("Expresion en Software");
        //List<CfdiSearchResult> lstCfdiFilteredByRfc = facturama.Cfdis().ListFilterByRfc("ESO1202108R2");                
        //System.out.println("Se obtiene la lista de facturas: " + lstCfdiFilteredByKeyword.size());
        //System.out.println("Se obtiene la lista de facturas por RFC: " + lstCfdiFilteredByRfc.size());
        System.out.println("----- Fin del ejemplo de CFDI -----");

    }

    private static void sampleCfdiGlobal(FacturamaApi facturama) throws IOException, FacturamaException, Exception {

        System.out.println("----- Inicio del ejemplo de CFDI 4.0 -----");

        // -------- Creacion del cfdi en su forma general (sin items / productos) asociados --------
        com.Facturama.sdk_java.Models.Request.Cfdi cfdi = createModelCfdiGeneral40Global(facturama);

        // -------- Agregar los items que lleva el cfdi ( para este ejemplo, se agregan con datos aleatorios) --------        
        //cfdi = addItemsToCfdi(facturama, cfdi);
        cfdi = addStaticItemsToCfdi(facturama, cfdi);

        // Se obtiene la factura recien creada
        com.Facturama.sdk_java.Models.Response.Cfdi cfdiCreated = facturama.Cfdis().Create3(cfdi);

        System.out.println("Se creó exitosamente el cfdi con el folio fiscal: " + cfdiCreated.getComplement().getTaxStamp().getUuid());

        // Descarga de los archivos de la factura
        //String filePath = "factura"+cfdiCreated.getComplement().getTaxStamp().getUuid();
        //facturama.Cfdis().SavePdf(filePath+".pdf", cfdiCreated.getId());
        //facturama.Cfdis().SaveXml(filePath+".xml", cfdiCreated.getId());
        // Se elmina la factura recien creada
        //facturama.Cfdis().Remove(cfdiCreated.getId(),"issued","01","d8e34bab-5bd4-4788-bde2-1428dc469e10");        
        //System.out.println( "Se elminó exitosamente el cfdi con el folio fiscal: " +  cfdiCreated.getComplement().getTaxStamp().getUuid() );
        //El correo que se ingrese debe existir 
        //System.out.println(facturama.Cfdis().SendEmail("example@.mx",CfdiService.InvoiceType.Issued, "7eo51BvzV-E16gBx3nnxfQ2"));
        // Consulta de cfdis mediante palabra clave o rfc
        //List<CfdiSearchResult> lstCfdiFilteredByKeyword = facturama.Cfdis().List("Expresion en Software");
        //List<CfdiSearchResult> lstCfdiFilteredByRfc = facturama.Cfdis().ListFilterByRfc("ESO1202108R2");                
        //System.out.println("Se obtiene la lista de facturas: " + lstCfdiFilteredByKeyword.size());
        //System.out.println("Se obtiene la lista de facturas por RFC: " + lstCfdiFilteredByRfc.size());
        System.out.println("----- Fin del ejemplo de CFDI -----");

    }

    /*
     * Llenado del modelo de CFDI usando catalogos y consultas
     * - Se especifica: la moneda, método de pago, forma de pago, cliente, y lugar de expedición     
     */
    private static com.Facturama.sdk_java.Models.Request.Cfdi createModelCfdiGeneral(FacturamaApi facturama) throws IOException, FacturamaException, Exception {

        Currency currency = facturama.Catalogs().Currency("MXN");

        com.Facturama.sdk_java.Models.Request.Cfdi cfdi = new com.Facturama.sdk_java.Models.Request.Cfdi();
        // Lista del catálogo de nombres en el PDF
        NameCfdi nameForPdf = facturama.Catalogs().NameIds().get(0); // Nombre en el pdf: "Factura"

        // Método de pago                             
        Catalog paymentMethod = facturama.Catalogs().PaymentMethod("PUE");

        // Forma de pago
        Catalog paymentForm = facturama.Catalogs().PaymentForms().stream().filter(p -> p.getName().equals("Efectivo")).findFirst().get();

        // Cliente (se toma como cliente el "cliente generico", aquel que tiene el RFC genérico),
        // (como los clientes son exclusivos para cada usuario, se debe previamente dar de alta este cliente)
        Client client = facturama.Clients().List().stream().filter(p -> p.getRfc().equals("URE180429TM6")).findFirst().get();

        // Lugar de expedición (es necesario por lo menos tener una sucursal)
        BranchOffice branchOffice = facturama.BranchOffices().List().get(0);

        cfdi.setNameId(nameForPdf.getValue());
        cfdi.setFolio("100");
        cfdi.setCfdiType(CfdiType.Ingreso.getValue());
        cfdi.setPaymentForm(paymentForm.getValue());
        cfdi.setPaymentMethod(paymentMethod.getValue());
        cfdi.setCurrency(currency.getValue());
        cfdi.setExpeditionPlace(branchOffice.getAddress().getZipCode());

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        cfdi.setDate(dateFormat.format(date));

        Receiver receiver = new Receiver();
        receiver.setCfdiUse(client.getCfdiUse());
        receiver.setName(client.getName());
        receiver.setRfc(client.getRfc());
        receiver.setFiscalRegime(client.getFiscalRegime());
        receiver.setTaxZipCode(client.getTaxZipCode());

        cfdi.setReceiver(receiver);

        return cfdi;

    }

    /*
    *   Llenado del CFDI 4.0
    *   Se especifica: la moneda, método de pago, forma de pago, cliente, y lugar de expedición 
     */
    private static com.Facturama.sdk_java.Models.Request.Cfdi createModelCfdiGeneral40(FacturamaApi facturama) throws IOException, FacturamaException, Exception {

        com.Facturama.sdk_java.Models.Request.Cfdi cfdi = new com.Facturama.sdk_java.Models.Request.Cfdi();
        
        Random randomNumbers = new Random();
        int RandomFolio = randomNumbers.nextInt(999) + 1;
        String nFolio = "FCA" + RandomFolio;
        cfdi.setFolio(nFolio);

        cfdi.setNameId("1");
        //cfdi.setSerie("FAC");
        cfdi.setCfdiType("I");
        cfdi.setPaymentForm("03");
        cfdi.setPaymentMethod("PUE");
        cfdi.setOrderNumber("TEST-001");
        cfdi.setCurrency("MXN");
        cfdi.setExpeditionPlace("78000");
        cfdi.setExportation("01");
        cfdi.setObservations("Elemento Observaciones solo visible en PDF");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        cfdi.setDate(dateFormat.format(date));

        Receiver receiver = new Receiver();
        receiver.setRfc("ZUÑ920208KL4");
        receiver.setName("ZAPATERIA URTADO ÑERI");
        receiver.setCfdiUse("G03");
        receiver.setTaxZipCode("34541");
        receiver.setFiscalRegime("601");

        cfdi.setReceiver(receiver);

        return cfdi;

    }

    private static com.Facturama.sdk_java.Models.Request.Cfdi createModelCfdiGeneral40Global(FacturamaApi facturama) throws IOException, FacturamaException, Exception {

        com.Facturama.sdk_java.Models.Request.Cfdi cfdi = new com.Facturama.sdk_java.Models.Request.Cfdi();
        
        Random randomNumbers = new Random();
        int RandomFolio = randomNumbers.nextInt(999) + 1;
        String nFolio = "GLOBAL" + RandomFolio;
        cfdi.setFolio(nFolio);

        cfdi.setNameId("1");
        cfdi.setSerie("FAC");
        cfdi.setCfdiType("I");
        cfdi.setPaymentForm("03");
        cfdi.setPaymentMethod("PUE");
        cfdi.setOrderNumber("TEST-001");
        cfdi.setCurrency("MXN");
        cfdi.setExpeditionPlace("78000");
        cfdi.setExportation("01");
        cfdi.setObservations("Elemento Observaciones solo visible en PDF");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        cfdi.setDate(dateFormat.format(date));

        //Nodo Informacion Global
        GlobalInformation globalinformation = new GlobalInformation();
        globalinformation.setPeriodicity("01");
        globalinformation.setMonths("05");
        globalinformation.setYear("2024");
        cfdi.setGlobalInformation(globalinformation);

        Receiver receiver = new Receiver();
        receiver.setRfc("XAXX010101000");
        receiver.setName("PUBLICO EN GENERAL");
        receiver.setCfdiUse("S01");
        receiver.setTaxZipCode("78000");
        receiver.setFiscalRegime("616");

        cfdi.setReceiver(receiver);

        return cfdi;

    }

    private static com.Facturama.sdk_java.Models.Request.Cfdi addItemsToCfdi(FacturamaApi facturama,
            com.Facturama.sdk_java.Models.Request.Cfdi cfdi) throws IOException, FacturamaException, Exception {

        // Lista de todos los productos
        List<Product> lstProducts = facturama.Products().List();
        Random random = new Random();
        Currency currency = facturama.Catalogs().Currency("MXN");

        int nItems = (random.nextInt(lstProducts.size()) % 10) + 1;
        int decimals = (int) currency.getDecimals();

        // Lista de Items en el cfdi (los articulos a facturar)
        List<Item> lstItems = new ArrayList<>();

        // Creacion del CFDI 
        for (int i = lstProducts.size() - nItems; i < lstProducts.size() && i > 0; i++) {

            Product product = lstProducts.get(i);   // Un producto cualquiera
            int quantity = random.nextInt(5) + 1;   // una cantidad aleatoria de elementos de este producto
            Double discount = product.getPrice() % (product.getPrice() == 0 ? 1 : random.nextInt((int) product.getPrice()));

            // Redondeo del precio del producto, de acuerdo a la moneda
            Double numberOfDecimals = Math.pow(10, decimals);
            Double subTotal = Math.round((product.getPrice() * quantity) * numberOfDecimals) / numberOfDecimals;

            // Llenado del item (que va en el cfdi)
            Item item = new Item();
            item.setProductCode(product.getCodeProdServ());
            item.setUnitCode(product.getUnitCode());
            item.setUnit(product.getUnit());
            item.setDescription(product.getDescription());
            item.setIdentificationNumber(product.getIdentificationNumber());
            item.setQuantity(quantity);
            item.setDiscount(Math.round(discount * numberOfDecimals) / numberOfDecimals);
            item.setUnitPrice(Math.round(product.getPrice() * numberOfDecimals) / numberOfDecimals);
            item.setSubtotal(subTotal);
            item.setTaxObject("02");// Nuevo elemento CFDI 4.0

            //A cuenta de terceros
            /*item.setCuentaPredial("12345");
            
            ThirdPartyAccount thirdPartyAccount= new ThirdPartyAccount(); 
            thirdPartyAccount.setRfc("CACX7605101P8");
            thirdPartyAccount.setName("XOCHILT CASAS CHAVEZ");
            thirdPartyAccount.setFiscalRegime("616");
            thirdPartyAccount.setTaxZipCode("10740");
            
            
           item.setThirdPartyAccount(thirdPartyAccount);
             */
            //Número de Pedimento
            /*
            ArrayList<String> numerosPedimento=new ArrayList<>();
            numerosPedimento.add("21  47  3807  8003832");
            numerosPedimento.add("21  47  3807  8003832");
            numerosPedimento.add("21  47  3807  8003832");
            
            item.setNumerosPedimento(numerosPedimento);
             */
            // ---- Llenado de los impuestos del item ----                                    
            item = addTaxesToItem(item, product, numberOfDecimals);

            lstItems.add(item);

        }

        cfdi.setItems(lstItems);

        return cfdi;
    }

    private static com.Facturama.sdk_java.Models.Request.Cfdi addStaticItemsToCfdi(FacturamaApi facturama, com.Facturama.sdk_java.Models.Request.Cfdi cfdi) throws IOException, FacturamaException, Exception {

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

        //A cuenta de terceros
        /*item.setCuentaPredial("12345");
            
            ThirdPartyAccount thirdPartyAccount= new ThirdPartyAccount(); 
            thirdPartyAccount.setRfc("CACX7605101P8");
            thirdPartyAccount.setName("XOCHILT CASAS CHAVEZ");
            thirdPartyAccount.setFiscalRegime("616");
            thirdPartyAccount.setTaxZipCode("10740");
            
            
           item.setThirdPartyAccount(thirdPartyAccount);
         */
        //Número de Pedimento
        /*
            ArrayList<String> numerosPedimento=new ArrayList<>();
            numerosPedimento.add("21  47  3807  8003832");
            numerosPedimento.add("21  47  3807  8003832");
            numerosPedimento.add("21  47  3807  8003832");
            
            item.setNumerosPedimento(numerosPedimento);
         */
        return cfdi;
    }

    /*
     * Se agregan los impuestos al Item (uno de los items del cfdi)
     * Se agregan todos los impuestos del producto, en el caso de que no se tengan impuestos, se debe colocar un valor nulo
     */
    private static Item addTaxesToItem(Item item, Product product, Double numberOfDecimals) {

        List<ProductTax> lstProductTaxes = product.getTaxes(); // impuestos del producto
        List<Tax> lstTaxes = new ArrayList<>();              // Impuestos del item (del cfdi)

        Double baseAmount = Math.round((item.getSubtotal() - item.getDiscount()) * numberOfDecimals) / numberOfDecimals;

        for (int j = 0; j < lstProductTaxes.size(); j++) {

            ProductTax pTax = lstProductTaxes.get(j);

            Tax tax = new Tax();

            tax.setName(pTax.getName());
            tax.setIsQuota(pTax.getIsQuota());
            tax.setIsRetention(pTax.getIsRetention());
            if (pTax.getRate() == null) {
                pTax.setRate(0d);
            }

            Double rate = pTax.getRate();
            Double rateRounded = (double) Math.round(rate * 1000000) / 1000000;
            tax.setRate(rateRounded);
            tax.setBase(Math.round(item.getSubtotal() * numberOfDecimals) / numberOfDecimals);
            tax.setTotal(Math.round((/*cambie el baseAmount*/tax.getBase() * pTax.getRate()) * numberOfDecimals) / numberOfDecimals);

            lstTaxes.add(tax);
        }

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

        return item;

    }

    /*
     * Ejemplo de creación de un CFDI "complemento de pago"
     * Referencia: https://apisandbox.facturama.mx/guias/api-web/cfdi/complemento-pago
     * 
     * En virtud de que el complemento de pago, requiere ser asociado a un CFDI con el campo "PaymentMethod" = "PPD"
     * En este ejemplo se incluye la creacón de este CFDI, para posteriormente realizar el  "Complemento de pago" = "PUE"     
     */
    private static void samplePaymentComplement(FacturamaApi facturama) throws IOException, FacturamaException, Exception {

        System.out.println("----- Inicio del ejemplo samplePaymentComplement -----");

        System.out.println("Creación del CFDI Inicial (PPD)");
        // Cfdi Incial (debe ser "PPD")
        // -------- Creacion del cfdi en su forma general (sin items / productos) asociados --------
        com.Facturama.sdk_java.Models.Request.Cfdi cfdi = createModelCfdiGeneral40(facturama);

        // -------- Agregar los items que lleva el cfdi ( para este ejemplo, se agregan con datos aleatorios) --------        
        cfdi = addStaticItemsToCfdi(facturama, cfdi);

        cfdi.setPaymentMethod("PPD");                   // El método de pago del documento inicial debe ser "PPD"
        cfdi.setPaymentForm("99");
        
        Random randomNumbers = new Random();
        int RandomFolio = randomNumbers.nextInt(999) + 1;
        String nFolio = "COMPAGO" + RandomFolio;
        cfdi.setFolio(nFolio);

        // Se manda timbrar mediante Facturama
        com.Facturama.sdk_java.Models.Response.Cfdi cfdiInicial = facturama.Cfdis().Create3(cfdi);

        System.out.println("Se creó exitosamente el cfdi Inicial (PPD) con el folio fiscal: " + cfdiInicial.getComplement().getTaxStamp().getUuid());

        // Descarga de los archivos del documento inicial
//        String filePath = "factura" + cfdiInicial.getComplement().getTaxStamp().getUuid();
//        facturama.Cfdis().SavePdf(filePath + ".pdf", cfdiInicial.getId());
//        facturama.Cfdis().SaveXml(filePath + ".xml", cfdiInicial.getId());

        // Complemento de pago (debe ser "PUE")        
        // Y no lleva "Items" solo especifica el "Complemento"
        System.out.println("Creación del complemento de Pago (PUE)");

        com.Facturama.sdk_java.Models.Request.Cfdi paymentComplementModel = createModelCfdiPaymentComplement(facturama, cfdiInicial);

        // Se manda timbrar el complemento de pago mediante Facturama
        com.Facturama.sdk_java.Models.Response.Cfdi paymentComplement = facturama.Cfdis().Create3(paymentComplementModel);

        System.out.println("Se creó exitosamente el complemento de pago con el folio fiscal: " + paymentComplement.getComplement().getTaxStamp().getUuid());

        // Descarga de los archivos del documento inicial
//        String filePathPayment = "factura" + paymentComplement.getComplement().getTaxStamp().getUuid();
//        facturama.Cfdis().SavePdf(filePath + ".pdf", paymentComplement.getId());
//        facturama.Cfdis().SaveXml(filePath + ".xml", paymentComplement.getId());

        // Posibilidad de mandar  los cfdis por coreo ( el cfdiInical y complemento de pago)
//        System.out.println(facturama.Cfdis().SendEmail("ejemplo@ejemplo.mx", CfdiService.InvoiceType.Issued, cfdiInicial.getId()));
//        System.out.println(facturama.Cfdis().SendEmail("ejemplo@ejemplo.mx", CfdiService.InvoiceType.Issued, paymentComplement.getId()));

        System.out.println("----- Fin del ejemplo de samplePaymentComplement -----");

    }

    /*
     * Modelo "Complemento de pago"
     * - Se especifica: la moneda, método de pago, forma de pago, cliente, y lugar de expedición     
     */
    private static com.Facturama.sdk_java.Models.Request.Cfdi
            createModelCfdiPaymentComplement(FacturamaApi facturama, com.Facturama.sdk_java.Models.Response.Cfdi cfdiInicial)
            throws IOException, FacturamaException, Exception {

        com.Facturama.sdk_java.Models.Request.Cfdi cfdi = new com.Facturama.sdk_java.Models.Request.Cfdi();

        // Lista del catálogo de nombres en el PDF
        NameCfdi nameForPdf = facturama.Catalogs().NameIds().get(13); // Nombre en el pdf: "Complemento de pago"

        // Forma de pago
        Catalog paymentForm = facturama.Catalogs().PaymentForms().stream().filter(p -> p.getName().equals("Por definir")).findFirst().get();

        cfdi.setNameId(nameForPdf.getValue());
        cfdi.setCfdiType(CfdiType.Pago.getValue()); // "P"                               

        // Receptor de comprobante (se toma como cliente el mismo a quien se emitió el CFDI Inicial),            
        String clientRfc = cfdiInicial.getReceiver().getRfc();
        Client client = facturama.Clients().List().stream().
                filter(p -> p.getRfc().equals(clientRfc))
                .findFirst().get();
        
        Random randomNumbers = new Random();
        int RandomFolio = randomNumbers.nextInt(999) + 1;
        String nFolio = "COMP" + RandomFolio;
        cfdi.setFolio(nFolio);

        Receiver receiver = new Receiver();
        receiver.setRfc("ZUÑ920208KL4");
        receiver.setName("ZAPATERIA URTADO ÑERI");
        receiver.setCfdiUse("CP01");
        receiver.setTaxZipCode("34541");
        receiver.setFiscalRegime("601");
        cfdi.setReceiver(receiver);

        // Lugar de expedición (es necesario por lo menos tener una sucursal)
        BranchOffice branchOffice = facturama.BranchOffices().List().get(0);
        cfdi.setExpeditionPlace(branchOffice.getAddress().getZipCode());

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
        pago.setPaymentForm("03");

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
    private static com.Facturama.sdk_java.Models.Request.Cfdi addIneComplement(FacturamaApi facturama,
            com.Facturama.sdk_java.Models.Request.Cfdi cfdi) throws IOException, FacturamaException, Exception {

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

        entidad.setContabilidad(lstContabilidad);
        lstentidad.add(entidad);

        ine.setEntidad(lstentidad);

        complement.setIne(ine);
        cfdi.setComplements(complement);

        return cfdi;
    }
}
