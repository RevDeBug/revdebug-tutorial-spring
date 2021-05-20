INSERT INTO "Accounts" ("AccountId", "Balance", "FirstName", "LastName") VALUES
    (1, 1024,'Exceptional','LLC'),
    (2, 2048,'BadData','LLC'),
    (3, 4096,'Upright','S corp');

INSERT INTO "Products" ("ProductId", "Label", "Description", "UnitPrice", "Tax") VALUES
    (1, 'Nimbus2000', 'BroomStick', 3000, 23),
    (2, 'T1000', 'Housework helping machine', 5999, 0),
    (3, 'The One Ring', 'Makes you disappear', 1999, 0),
    (4, 'Volley ball', 'Wilson??', 19, 5),
    (5, 'Mjolnir', 'Nailed it', 12335, 15);

INSERT INTO "Invoices" ("Accepted", "Reconciled", "Number", "InvoiceId", "AccountId", "IssueDate", "DueDate") VALUES
    (false, false, 1, concat('222/ST/',  year(now())), 1, DATEADD(DAY, -25, DATEADD(HOUR, -4,  DATEADD(MINUTE, 20, now()))), DATEADD(DAY, 25, DATEADD(HOUR, -4,  DATEADD(MINUTE, 20, now())))),
    (false, false, 2, concat('88/SU/05/',  year(now())), 3, DATEADD(DAY, -12, DATEADD(HOUR, -2,  DATEADD(MINUTE, 45, now()))), DATEADD(DAY, 15, DATEADD(HOUR, -4,  DATEADD(MINUTE, 20, now())))),
    (false, false, 3, concat('135/ST/GD/05/',  year(now())), 2, DATEADD(DAY, -16, DATEADD(HOUR, -9,  DATEADD(MINUTE, 24, now()))), DATEADD(DAY, 8, DATEADD(HOUR, -4,  DATEADD(MINUTE, 20, now()))));

INSERT INTO "InvoiceEntries" ("InvoiceEntryId", "InvoiceId", "ProductId", "Quantity") VALUES
    (1, concat('222/ST/',  year(now())), 1, 5),
    (2, concat('222/ST/',  year(now())), 2, 20),
    (3, concat('222/ST/',  year(now())), 5, 1),
    (4, concat('88/SU/05/',  year(now())), 3, 7),
    (5, concat('88/SU/05/',  year(now())), 5, 5),
    (6, concat('88/SU/05/',  year(now())), 4, 8),
    (7, concat('135/ST/GD/05/',  year(now())), 2, 10),
    (8, concat('135/ST/GD/05/',  year(now())), 3, 1),
    (9, concat('135/ST/GD/05/',  year(now())), 4, 50);