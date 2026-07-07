package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PortfolioDao {
    @Query("SELECT * FROM portfolios ORDER BY createdAt DESC")
    fun getAllPortfolios(): Flow<List<Portfolio>>

    @Query("SELECT * FROM portfolios WHERE id = :id")
    suspend fun getPortfolioById(id: Int): Portfolio?

    @Query("SELECT * FROM portfolios WHERE id = :id")
    fun getPortfolioByIdFlow(id: Int): Flow<Portfolio?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPortfolio(portfolio: Portfolio): Long

    @Update
    suspend fun updatePortfolio(portfolio: Portfolio)

    @Delete
    suspend fun deletePortfolio(portfolio: Portfolio)

    @Query("DELETE FROM portfolios WHERE id = :id")
    suspend fun deletePortfolioById(id: Int)
}
