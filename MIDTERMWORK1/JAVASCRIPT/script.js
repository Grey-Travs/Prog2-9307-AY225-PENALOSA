const fs = require('fs');

class DataAnalyzer {
    static processFile(filePath) {
        try {
            const data = fs.readFileSync(filePath, 'utf-8');
            const lines = data.split('\n').filter(line => line.trim() !== '');

            if (lines.length <= 1) {
                return { success: false, message: "Dataset is empty or contains only headers." };
            }

            let totalRecords = 0;
            let totalSales = 0;
            let highestTransaction = -Infinity;
            let lowestTransaction = Infinity;

            for (let i = 1; i < lines.length; i++) {
                const columns = lines[i].split(',');
                if (columns.length > 7) {
                    const sales = parseFloat(columns[7]);
                    if (!isNaN(sales)) {
                        totalRecords++;
                        totalSales += sales;
                        if (sales > highestTransaction) highestTransaction = sales;
                        if (sales < lowestTransaction) lowestTransaction = sales;
                    }
                }
            }

            if (totalRecords === 0) return { success: false, message: "No valid data found." };

            const averageSales = totalSales / totalRecords;

            return {
                success: true,
                totalRecords: totalRecords.toLocaleString(),
                totalSales: totalSales.toFixed(2),
                averageSales: averageSales.toFixed(2),
                highestTransaction: highestTransaction.toFixed(2),
                lowestTransaction: lowestTransaction.toFixed(2)
            };

        } catch (error) {
            return { success: false, message: error.message };
        }
    }
}

module.exports = DataAnalyzer;