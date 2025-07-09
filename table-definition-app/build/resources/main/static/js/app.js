let columnIndex = 0;

document.addEventListener('DOMContentLoaded', function() {
    // Initialize column index based on existing columns
    const existingColumns = document.querySelectorAll('.column-row');
    columnIndex = existingColumns.length;
    
    // Add event listeners for primary key checkboxes
    document.addEventListener('change', function(e) {
        if (e.target.type === 'checkbox' && e.target.name && e.target.name.includes('primaryKey')) {
            if (e.target.checked) {
                // When primary key is checked, uncheck nullable
                const row = e.target.closest('tr');
                const nullableCheckbox = row.querySelector('input[name*="nullable"]');
                if (nullableCheckbox) {
                    nullableCheckbox.checked = false;
                }
            }
        }
    });
});

function addColumn() {
    const columnsList = document.getElementById('columnsList');
    const newRow = document.createElement('tr');
    newRow.className = 'column-row';
    
    newRow.innerHTML = `
        <input type="hidden" name="columns[${columnIndex}].id" value="">
        <input type="hidden" name="columns[${columnIndex}].columnOrder" value="${columnIndex + 1}">
        <td>
            <input type="text" class="form-control form-control-sm" 
                   name="columns[${columnIndex}].physicalName" required>
        </td>
        <td>
            <input type="text" class="form-control form-control-sm" 
                   name="columns[${columnIndex}].logicalName" required>
        </td>
        <td>
            <select class="form-select form-select-sm" 
                    name="columns[${columnIndex}].dataType" required>
                <option value="VARCHAR">VARCHAR</option>
                <option value="CHAR">CHAR</option>
                <option value="TEXT">TEXT</option>
                <option value="INTEGER">INTEGER</option>
                <option value="BIGINT">BIGINT</option>
                <option value="DECIMAL">DECIMAL</option>
                <option value="DATE">DATE</option>
                <option value="TIMESTAMP">TIMESTAMP</option>
                <option value="BOOLEAN">BOOLEAN</option>
            </select>
        </td>
        <td>
            <input type="number" class="form-control form-control-sm" 
                   name="columns[${columnIndex}].length">
        </td>
        <td class="text-center">
            <input type="checkbox" class="form-check-input" 
                   name="columns[${columnIndex}].nullable" checked>
        </td>
        <td class="text-center">
            <input type="checkbox" class="form-check-input" 
                   name="columns[${columnIndex}].primaryKey">
        </td>
        <td class="text-center">
            <input type="checkbox" class="form-check-input" 
                   name="columns[${columnIndex}].uniqueKey">
        </td>
        <td>
            <input type="text" class="form-control form-control-sm" 
                   name="columns[${columnIndex}].defaultValue">
        </td>
        <td>
            <input type="text" class="form-control form-control-sm" 
                   name="columns[${columnIndex}].description">
        </td>
        <td>
            <button type="button" class="btn btn-sm btn-danger" 
                    onclick="removeColumn(this)">×</button>
        </td>
    `;
    
    columnsList.appendChild(newRow);
    columnIndex++;
}

function removeColumn(button) {
    const row = button.closest('tr');
    row.remove();
    updateColumnIndices();
}

function updateColumnIndices() {
    const columnRows = document.querySelectorAll('.column-row');
    columnRows.forEach((row, index) => {
        const inputs = row.querySelectorAll('input, select');
        inputs.forEach(input => {
            if (input.name) {
                input.name = input.name.replace(/\[\d+\]/, `[${index}]`);
            }
        });
        
        // Update column order
        const columnOrderInput = row.querySelector('input[name*="columnOrder"]');
        if (columnOrderInput) {
            columnOrderInput.value = index + 1;
        }
    });
    
    columnIndex = columnRows.length;
}

// Export functions are now defined in the HTML template with correct project context

function getSelectedTableId() {
    const selectedRadio = document.querySelector('input[name="selectedTableId"]:checked');
    return selectedRadio ? selectedRadio.value : null;
}

// Delete function is now handled in the HTML template with correct project context