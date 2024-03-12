package br.com.log.service;

import br.com.log.model.Employee;
import br.com.log.model.Gender;
import br.com.log.repository.EmployeeRepository;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    private EmployeeService employeeService;
    @Mock
    private EmployeeRepository repository;

    @BeforeEach
    public void setup() {
        this.employeeService = new EmployeeServiceImpl(repository);
    }

    @Test
    void shouldTestcreateEmployeeWhenSucess() {
        Employee employee = new Employee();
        employee.setName("João Silva");
        employee.setId(1L);
        employee.setSalary(5000.0);
        employee.setAge(30);
        employee.setGender(Gender.MAN);

        // Simula o comportamento do repositório
        Mockito.when(repository.save(employee)).thenReturn(employee);

        // Chama o método a ser testado
        Employee employeeCriado = employeeService.createEmployee(employee);

        // Verifica se o funcionário foi criado com sucesso
        Mockito.verify(repository).save(employee);
        assertNotNull(employeeCriado);
        assertEquals("João Silva", employeeCriado.getName());
        assertEquals(5000.0, employeeCriado.getSalary(), 0.001);
    }

    @Test
    void shouldTestCreateEmployeeTestingLogSucess() {
        // Implementa o método para captura dos logs
        LogCaptor logCaptor = LogCaptor.forClass(EmployeeServiceImpl.class);

        Employee employee = new Employee();
        employee.setName("Paulo");
        employee.setId(1L);
        employee.setSalary(5000.0);
        employee.setAge(30);
        employee.setGender(Gender.MAN);

        Mockito.when(repository.save(employee)).thenReturn(employee);

        Employee employeeCriado = employeeService.createEmployee(employee);

        Mockito.verify(repository).save(employee);

        assertThat(logCaptor.getLogs())
                .hasSize(1)
                .contains("Salvando novo funcionário: {}Employee(id=1, name=Paulo, salary=5000.0, age=30, gender=MAN)");
    }

    @Test
    void shouldTestCreateEmployeeTestingLogInSucessX() {
        // Implementa o método para captura dos logs
        LogCaptor logCaptor = LogCaptor.forClass(EmployeeServiceImpl.class);

        Employee employee = new Employee();
        employee.setName(null);
        employee.setId(1L);
        employee.setSalary(5000.0);
        employee.setAge(30);
        employee.setGender(Gender.MAN);

        assertThrows(NullPointerException.class, () -> {
            employeeService.createEmployee(employee);
        });

        //Cenário de error
        assertThat(logCaptor.getLogs())
                .contains("Erro ao salvar funcionário: {}");
    }


}