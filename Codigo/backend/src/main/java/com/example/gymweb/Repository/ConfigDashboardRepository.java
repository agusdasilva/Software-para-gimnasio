package com.example.gymweb.Repository;

import com.example.gymweb.model.ConfigDashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigDashboardRepository extends JpaRepository<ConfigDashboard, Integer> {
}
