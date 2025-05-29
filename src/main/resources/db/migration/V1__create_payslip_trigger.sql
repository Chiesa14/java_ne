-- Create function to handle payslip approval
CREATE OR REPLACE FUNCTION handle_payslip_approval()
RETURNS TRIGGER AS $$
DECLARE
    v_first_name VARCHAR;
    v_email VARCHAR;
    v_message TEXT;
    v_institution_name VARCHAR;
BEGIN
    -- Only proceed if status changed to PAID
    IF NEW.status = 'PAID' AND (OLD.status IS NULL OR OLD.status != 'PAID') THEN
        -- Get employee details
        SELECT e.first_name, e.email, e.code
        INTO v_first_name, v_email, v_message
        FROM employee e
        WHERE e.code = NEW.employee_code;

        -- Get institution name from configuration
        SELECT value INTO v_institution_name
        FROM configuration
        WHERE key = 'institution.name';

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
        INSERT INTO message (
            code,
            employee_code,
            subject,
            content,
            status,
            created_at,
            updated_at
        ) VALUES (
            gen_random_uuid(),
            NEW.employee_code,
            'Salary Payment Notification',
            v_message,
            'UNREAD',
            CURRENT_TIMESTAMP,
            CURRENT_TIMESTAMP
        );

        -- Send email notification
        PERFORM pg_notify(
            'email_notification',
            json_build_object(
                'to', v_email,
                'subject', 'Salary Payment Notification',
                'content', v_message
            )::text
        );
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create trigger
DROP TRIGGER IF EXISTS payslip_approval_trigger ON pay_slip;
CREATE TRIGGER payslip_approval_trigger
    AFTER UPDATE ON pay_slip
    FOR EACH ROW
    EXECUTE FUNCTION handle_payslip_approval(); 