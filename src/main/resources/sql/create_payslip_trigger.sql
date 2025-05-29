-- Drop existing trigger and function if they exist
DROP TRIGGER IF EXISTS payslip_approval_trigger ON pay_slip;
DROP FUNCTION IF EXISTS handle_payslip_approval();

-- Create function to handle payslip approval
CREATE OR REPLACE FUNCTION handle_payslip_approval()
RETURNS TRIGGER AS $$
DECLARE
    v_first_name VARCHAR;
    v_email VARCHAR;
    v_message TEXT;
    v_institution_name VARCHAR := 'Rwanda Coding Academy'; -- Institution name from properties
BEGIN
    -- Only proceed if status changed to PAID
    IF NEW.status = 'PAID' AND (OLD.status IS NULL OR OLD.status != 'PAID') THEN
        -- Get employee details
        SELECT e.first_name, e.email
        INTO v_first_name, v_email
        FROM employee e
        WHERE e.code = NEW.employee_code;

        -- Create message
        v_message := format('Dear %s, your salary for %s/%s from %s amounting to %s has been credited to your account %s successfully.',
            v_first_name,
            NEW.month,
            NEW.year,
            v_institution_name,
            NEW.net_salary,
            NEW.employee_code
        );

        -- Insert message into messages table
        INSERT INTO messages (
            id,
            employee_id,
            message,
            month,
            year,
            created_at
        ) VALUES (
            gen_random_uuid(),
            NEW.employee_code,
            v_message,
            NEW.month,
            NEW.year,
            CURRENT_TIMESTAMP
        );
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create trigger
CREATE TRIGGER payslip_approval_trigger
    AFTER UPDATE ON pay_slip
    FOR EACH ROW
    EXECUTE FUNCTION handle_payslip_approval(); 