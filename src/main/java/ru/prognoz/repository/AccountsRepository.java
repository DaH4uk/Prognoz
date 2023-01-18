package ru.prognoz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.prognoz.entities.AccountEntity;

@Service
public interface AccountsRepository extends JpaRepository<AccountEntity, Long> {
}
