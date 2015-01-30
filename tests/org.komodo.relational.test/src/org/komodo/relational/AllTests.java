package org.komodo.relational;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.komodo.relational.model.internal.AccessPatternImplTest;
import org.komodo.relational.model.internal.ColumnImplTest;
import org.komodo.relational.model.internal.ForeignKeyImplTest;
import org.komodo.relational.model.internal.IndexImplTest;
import org.komodo.relational.model.internal.ModelImplTest;
import org.komodo.relational.model.internal.ParameterImplTest;
import org.komodo.relational.model.internal.PrimaryKeyImplTest;
import org.komodo.relational.model.internal.ProcedureImplTest;
import org.komodo.relational.model.internal.ProcedureResultSetImplTest;
import org.komodo.relational.model.internal.StatementOptionImplTest;
import org.komodo.relational.model.internal.TableImplTest;
import org.komodo.relational.model.internal.UniqueConstraintImplTest;
import org.komodo.relational.model.internal.ViewImplTest;
import org.komodo.relational.model.legacy.TestAccessPattern;
import org.komodo.relational.model.legacy.TestColumn;
import org.komodo.relational.model.legacy.TestForeignKey;
import org.komodo.relational.model.legacy.TestModel;
import org.komodo.relational.model.legacy.TestParameter;
import org.komodo.relational.model.legacy.TestPrimaryKey;
import org.komodo.relational.model.legacy.TestProcedure;
import org.komodo.relational.model.legacy.TestProcedureResultSet;
import org.komodo.relational.model.legacy.TestSchema;
import org.komodo.relational.model.legacy.TestTable;
import org.komodo.relational.model.legacy.TestUniqueConstraint;
import org.komodo.relational.model.legacy.TestView;
import org.komodo.relational.vdb.internal.ConditionImplTest;
import org.komodo.relational.vdb.internal.DataRoleImplTest;
import org.komodo.relational.vdb.internal.EntryImplTest;
import org.komodo.relational.vdb.internal.MaskImplTest;
import org.komodo.relational.vdb.internal.PermissionImplTest;
import org.komodo.relational.vdb.internal.TranslatorImplTest;
import org.komodo.relational.vdb.internal.VdbImplTest;
import org.komodo.relational.vdb.internal.VdbImportImplTest;
import org.komodo.relational.workspace.WorkspaceManagerTest;


/**
 * Suite for all unit tests
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	TestTable.class,
	TestColumn.class,
	TestProcedure.class,
	TestParameter.class,
	TestAccessPattern.class,
	TestForeignKey.class,
	TestModel.class,
	TestPrimaryKey.class,
	TestProcedureResultSet.class,
	TestSchema.class,
	TestUniqueConstraint.class,
	TestView.class,
	AccessPatternImplTest.class,
	ColumnImplTest.class,
	ForeignKeyImplTest.class,
    IndexImplTest.class,
    ModelImplTest.class,
    ParameterImplTest.class,
    PrimaryKeyImplTest.class,
    ProcedureImplTest.class,
    ProcedureResultSetImplTest.class,
    StatementOptionImplTest.class,
    TableImplTest.class,
    UniqueConstraintImplTest.class,
    ViewImplTest.class,
    ConditionImplTest.class,
    DataRoleImplTest.class,
    EntryImplTest.class,
    MaskImplTest.class,
    PermissionImplTest.class,
    TranslatorImplTest.class,
    VdbImplTest.class,
    VdbImportImplTest.class,
    WorkspaceManagerTest.class,
    })
@Ignore
public class AllTests {
    // nothing to do
}
