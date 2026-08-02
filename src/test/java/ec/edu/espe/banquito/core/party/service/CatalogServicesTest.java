package ec.edu.espe.banquito.core.party.service;

import ec.edu.espe.banquito.core.party.enums.CustomerStatusEnum;
import ec.edu.espe.banquito.core.party.enums.CustomerTypeEnum;
import ec.edu.espe.banquito.core.party.model.CoreParameter;
import ec.edu.espe.banquito.core.party.model.CustomerSubtype;
import ec.edu.espe.banquito.core.party.model.Holiday;
import ec.edu.espe.banquito.core.party.repository.CoreParameterRepository;
import ec.edu.espe.banquito.core.party.repository.CustomerSubtypeRepository;
import ec.edu.espe.banquito.core.party.repository.HolidayRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CatalogServicesTest {

    @Test
    void customerSubtypeService_debeMapearSubtipos() {
        CustomerSubtypeRepository repository = mock(CustomerSubtypeRepository.class);
        CustomerSubtypeService service = new CustomerSubtypeService(repository);
        CustomerSubtype subtype = new CustomerSubtype();
        subtype.setId(1);
        subtype.setCustomerType(CustomerTypeEnum.NATURAL);
        subtype.setName("Persona natural");
        subtype.setDescription("Cliente persona natural");
        subtype.setStatus(CustomerStatusEnum.ACTIVO);
        when(repository.findAll()).thenReturn(List.of(subtype));

        var result = service.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(1);
        assertThat(result.getFirst().getName()).isEqualTo("Persona natural");
    }

    @Test
    void coreParameterService_debeMapearParametros() {
        CoreParameterRepository repository = mock(CoreParameterRepository.class);
        CoreParameterService service = new CoreParameterService(repository);
        CoreParameter parameter = new CoreParameter();
        parameter.setCode("MAX_TRANSFER");
        parameter.setName("Monto maximo");
        parameter.setValueString("1000");
        parameter.setDataType("NUMBER");
        parameter.setDescription("Monto maximo diario");
        parameter.setLastUpdate(LocalDateTime.of(2026, 7, 1, 10, 0));
        when(repository.findAll()).thenReturn(List.of(parameter));

        var result = service.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getCode()).isEqualTo("MAX_TRANSFER");
        assertThat(result.getFirst().getValueString()).isEqualTo("1000");
    }

    @Test
    void holidayService_debeOrdenarYMapearFeriados() {
        HolidayRepository repository = mock(HolidayRepository.class);
        HolidayService service = new HolidayService(repository);
        Holiday later = holiday(LocalDate.of(2026, 12, 25), "Navidad");
        Holiday earlier = holiday(LocalDate.of(2026, 1, 1), "Anio Nuevo");
        when(repository.findAll()).thenReturn(List.of(later, earlier));

        var result = service.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.getFirst().getHolidayDate()).isEqualTo(LocalDate.of(2026, 1, 1));
        assertThat(result.get(1).getHolidayDate()).isEqualTo(LocalDate.of(2026, 12, 25));
    }

    private Holiday holiday(LocalDate date, String name) {
        Holiday holiday = new Holiday();
        holiday.setHolidayDate(date);
        holiday.setName(name);
        holiday.setIsWeekend(false);
        holiday.setCreationDate(LocalDateTime.of(2026, 1, 1, 0, 0));
        return holiday;
    }
}
