package es.uji.ei1027.SgOviProject.dto;

import es.uji.ei1027.SgOviProject.enums.StaffType;
import es.uji.ei1027.SgOviProject.model.Contract;

public class ContractListAllDTO {

    private Contract contract;
    private StaffType assistantType;
    private String assistantName;

    public ContractListAllDTO(Contract contract, StaffType assistantType, String name){
        this.contract = contract;
        this.assistantType = assistantType;
        this.assistantName = name;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public StaffType getAssistantType() {
        return assistantType;
    }

    public void setAssistantType(StaffType assistantType) {
        this.assistantType = assistantType;
    }

    public String getAssistantName() {
        return assistantName;
    }

    public void setAssistantName(String assistantName) {
        this.assistantName = assistantName;
    }

    @Override
    public String toString() {
        return "ContractListAllDTO{" +
                "contract=" + contract +
                ", assistantType=" + assistantType +
                ", assistantName='" + assistantName + '\'' +
                '}';
    }
}
