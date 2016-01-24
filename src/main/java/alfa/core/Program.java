/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alfa.core;

import alfa.core.runtime.AlfaEnvironment;
import alfa.util.Util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.antlr.v4.runtime.Token;

/**
 *
 * @author tdk
 */
public class Program implements PTreeNode, SymbolTable, EvaluationContext
{

    private final Object                    m_null;
    private final Map< String, Object>      m_values;
    private final Map< String, Exp>         m_varDefs;
    private final List<Statement>           m_statements;
    private AlfaEnvironment                 m_envir;
    private Statement                       m_currentStatement;

    private static boolean                  s_debug;

    public static void SetDebug( boolean debug )
    {
        s_debug = debug;
    }
    
  
    public Program()
    {
        m_statements = new ArrayList();
        m_varDefs = new HashMap();
        m_values = new HashMap();
        m_null = new Object();
    }

    public void exec( AlfaEnvironment envir )
    {
         m_envir = envir;
        resolve( this );
        for( Statement s : m_statements )
        {
            m_currentStatement = s;
             boolean dbg = s_debug;
            if( dbg )
            {
                System.out.println( s );
            }
            Object o = s.eval( this );
            if( dbg )
            {
                System.out.println( "---> " + o );
            }
        }
    }
    
    @Override
    public Statement getCurrentStatement()
    {
        return m_currentStatement;
    }

    public void add( Statement s )
    {
        m_statements.add( s );
    }

    @Override
    public Token getToken()
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public void resolve( SymbolTable st )
    {
        for( Statement s : m_statements )
        {
            s.resolve( st );
        }
    }

    @Override
    public AlfaEnvironment getEnvir()
    {
        return m_envir;
    }

    @Override
    public void define( String varName, Exp definition )
    {
        m_varDefs.put( varName, definition );
    }

    @Override
    public Exp getDefinition( String varName )
    {
        return m_varDefs.get( varName );
    }

    @Override
    public String toString()
    {
        String str = "";

        for( Statement s : m_statements )
        {
            str += s + "\n";
        }
        return str;
    }

    @Override
    public void setValue( String varName, Object value )
    {
        if( value == null )
        {
            value = m_null;
        }
        m_values.put( varName, value );
    }

    @Override
    public Object getValue( String varName, Token token )
    {
        if(  ! m_values.containsKey( varName ) )
        {
            throw new RuntimeException( "Unset variable " + varName + Util.At( token ) );
        }
        Object v = m_values.get( varName );
        if( v == m_null )
        {
            return null;
        }
        return v;
    }

    @Override
    public Object eval( EvaluationContext ctx )
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<String> getAllVars()
    {
        return this.m_varDefs.keySet();
    }

    @Override
    public boolean hasValue( String varName )
    {
        return m_values.containsKey( varName );
    }

}
