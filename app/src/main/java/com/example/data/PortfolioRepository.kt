package com.example.data

import kotlinx.coroutines.flow.Flow

class PortfolioRepository(private val portfolioDao: PortfolioDao) {
    val allPortfolios: Flow<List<Portfolio>> = portfolioDao.getAllPortfolios()

    fun getPortfolioByIdFlow(id: Int): Flow<Portfolio?> = portfolioDao.getPortfolioByIdFlow(id)

    suspend fun getPortfolioById(id: Int): Portfolio? = portfolioDao.getPortfolioById(id)

    suspend fun insertPortfolio(portfolio: Portfolio): Long = portfolioDao.insertPortfolio(portfolio)

    suspend fun updatePortfolio(portfolio: Portfolio) = portfolioDao.updatePortfolio(portfolio)

    suspend fun deletePortfolio(portfolio: Portfolio) = portfolioDao.deletePortfolio(portfolio)

    suspend fun deletePortfolioById(id: Int) = portfolioDao.deletePortfolioById(id)
}
