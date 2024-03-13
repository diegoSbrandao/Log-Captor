package br.com.log.service;

import br.com.log.model.Employee;
import br.com.log.repository.EmployeeRepository;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Log4j2
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Employee createEmployee(Employee employee) {
        try {
            if (Objects.nonNull(employee) && Objects.nonNull(employee.getName())) {
                log.info("Salvando novo funcionário: " + employee);
                return repository.save(employee);
            }
            throw new NullPointerException("Nome não pode ser nulo");
        } catch (ConstraintViolationException | NullPointerException e) {
            log.error("Erro ao salvar funcionário: ");
            throw new NullPointerException("Erro ao salvar funcionário: " + e.getMessage());
        }
    }
}
