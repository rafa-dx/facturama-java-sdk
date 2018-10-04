
package com.Facturama.sdk_java.Models.Request.Complements.ForeingTrade;

import java.util.List;

public class Comodity {
    
    public List<SpecificDescriptions> SpecificDescriptions;
    public String IdentificationNumber;
    public String TariffFraction;
    public Double CustomsQuantity;
    public String CustomsUnit;
    public Double CustomsUnitValue;
    public double ValueInDolar;
    
    public List<SpecificDescriptions> getSpecificDescriptions()
    {
        return SpecificDescriptions; 
    }
    
    public void setSpecificDescriptions(List<SpecificDescriptions> SpecificDescriptions)
    {
        this.SpecificDescriptions = SpecificDescriptions;
    }
    
    public String getIdentificationNumber()
    {
        return IdentificationNumber; 
    }
    
    public void setIdentificationNumber(String IdentificationNumber)
    {
        this.IdentificationNumber = IdentificationNumber;
    }
    
    public String getTariffFraction()
    {
        return TariffFraction; 
    }
    
    public void setTariffFraction(String TariffFraction)
    {
        this.TariffFraction = TariffFraction;
    }    
    
    public Double getCustomsQuantity()
    {
        return CustomsQuantity; 
    }
    
    public void setCustomsQuantity(Double CustomsQuantity)
    {
        this.CustomsQuantity = CustomsQuantity;
    }
    
    public String getCustomsUnit()
    {
        return CustomsUnit; 
    }
    
    public void setCustomsUnit(String CustomsUnit)
    {
        this.CustomsUnit = CustomsUnit;
    }      
    
    public Double getCustomsUnitValue()
    {
        return CustomsUnitValue; 
    }
    
    public void setCustomsUnitValue(Double CustomsUnitValue)
    {
        this.CustomsUnitValue = CustomsUnitValue;
    }
    
    public double getValueInDolar()
    {
        return ValueInDolar; 
    }
    
    public void setValueInDolar(double ValueInDolar)
    {
        this.ValueInDolar = ValueInDolar;
    }
}
