package ec.edu.espe.banquito.core.party.controller;

import ec.edu.espe.banquito.core.party.dto.BranchRequestDTO;
import ec.edu.espe.banquito.core.party.dto.BranchResponseDTO;
import ec.edu.espe.banquito.core.party.dto.CoreParameterResponseDTO;
import ec.edu.espe.banquito.core.party.dto.CustomerByAccountResponseDTO;
import ec.edu.espe.banquito.core.party.dto.CustomerRequestDTO;
import ec.edu.espe.banquito.core.party.dto.CustomerResponseDTO;
import ec.edu.espe.banquito.core.party.dto.CustomerSubtypeResponseDTO;
import ec.edu.espe.banquito.core.party.dto.HolidayResponseDTO;
import ec.edu.espe.banquito.core.party.service.BranchService;
import ec.edu.espe.banquito.core.party.service.CoreParameterService;
import ec.edu.espe.banquito.core.party.service.CustomerService;
import ec.edu.espe.banquito.core.party.service.CustomerSubtypeService;
import ec.edu.espe.banquito.core.party.service.HolidayService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ControllersTest {

    @Test
    void branchController_debeDelegarConsultasYCreacion() {
        BranchService service = mock(BranchService.class);
        BranchController controller = new BranchController(service);
        BranchRequestDTO request = new BranchRequestDTO();
        BranchResponseDTO response = new BranchResponseDTO(1, "001", "Sucursal Norte", "Quito");
        when(service.findAll()).thenReturn(List.of(response));
        when(service.create(request)).thenReturn(response);

        assertThat(controller.getBranches()).containsExactly(response);
        var created = controller.createBranch(request);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(created.getBody()).isSameAs(response);
    }

    @Test
    void customerController_debeDelegarOperaciones() {
        CustomerService service = mock(CustomerService.class);
        CustomerController controller = new CustomerController(service);
        CustomerRequestDTO request = new CustomerRequestDTO();
        CustomerResponseDTO response = new CustomerResponseDTO();
        CustomerByAccountResponseDTO byAccount = new CustomerByAccountResponseDTO();
        when(service.findByIdOrIdentification("1750285577")).thenReturn(response);
        when(service.create(request)).thenReturn(response);
        when(service.updateStatus("1", "INACTIVO")).thenReturn(response);
        when(service.findCustomerByAccountNumber("1010114999")).thenReturn(byAccount);

        assertThat(controller.getCustomer("1750285577")).isSameAs(response);
        assertThat(controller.createCustomer(request).getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(controller.updateCustomerStatus("1", "INACTIVO")).isSameAs(response);
        assertThat(controller.getCustomerByAccountNumber("1010114999")).isSameAs(byAccount);
    }

    @Test
    void catalogControllers_debenDelegarListados() {
        CustomerSubtypeService subtypeService = mock(CustomerSubtypeService.class);
        CoreParameterService parameterService = mock(CoreParameterService.class);
        HolidayService holidayService = mock(HolidayService.class);
        CustomerSubtypeResponseDTO subtype = new CustomerSubtypeResponseDTO();
        CoreParameterResponseDTO parameter = new CoreParameterResponseDTO();
        HolidayResponseDTO holiday = new HolidayResponseDTO(LocalDate.of(2026, 1, 1), "Anio Nuevo", false);
        when(subtypeService.findAll()).thenReturn(List.of(subtype));
        when(parameterService.findAll()).thenReturn(List.of(parameter));
        when(holidayService.findAll()).thenReturn(List.of(holiday));

        assertThat(new CustomerSubtypeController(subtypeService).getCustomerSubtypes()).containsExactly(subtype);
        assertThat(new CoreParameterController(parameterService).getCoreParameters()).containsExactly(parameter);
        assertThat(new HolidayController(holidayService).getHolidays()).containsExactly(holiday);
    }
}
