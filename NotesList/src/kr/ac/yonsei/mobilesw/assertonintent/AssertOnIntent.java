package kr.ac.yonsei.mobilesw.assertonintent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;

public class AssertOnIntent {
	
	IntentDataset intentDataset = null;
	Boolean checkIntent = false;

	//public boolean assertOnIntent(Context context, Intent intent, String assertString, MalformedIntentHandler handler)
	public boolean assertOnIntent(Intent intent, String assertString, MalformedIntentHandler handler)
	{
		long threadCpuTimeNanosStart = Debug.threadCpuTimeNanos();
		
		//already parse is done.
		if(intentDataset != null)
		{
			return checkIntent;
		}
		
		StaticParser<String> sp0 = new StaticParser<String>();

		try
		{
			intentDataset = sp0.parse(assertString);
			checkIntent = checkIntent(intent, intentDataset, handler);
			
			Log.i("IntentSpec", "Pass");
		}
		catch(MalformedIntentException m)
		{
			Log.i("IntentSpec", "Error Catch");
			Log.i("MalformedIntentException", m.getMsg() + " Exception number : " + m.getNumber() + ", " + sp0.exceptionIntentNumberMsg());

			if(handler != null)
			{
				long threadCpuTimeNanosEnd = Debug.threadCpuTimeNanos();
				Log.i("AssertOnIntent Processing Time", ((threadCpuTimeNanosEnd - threadCpuTimeNanosStart) / 1000000.0) + "ms");
				
				handler.handle(intent, m);
			}
			else
			{
				/*
				Intent diglogIntent = new Intent("android.intent.action.SHOWDIALOG");
				diglogIntent.addCategory(Intent.CATEGORY_LAUNCHER);
				diglogIntent.setComponent(new ComponentName("kr.ac.yonsei.mobilesw.assertonintent", "kr.ac.yonsei.mobilesw.assertonintent.MainActivity"));
				diglogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				diglogIntent.putExtra("msg", "MalformedIntentException. " + m.getMsg() + " Exception number : " + m.getNumber() + ", " + sp0.exceptionIntentNumberMsg());
				
				context.startActivity(diglogIntent);
				
				long threadCpuTimeNanosEnd = Debug.threadCpuTimeNanos();
				Log.i("AssertOnIntent Processing Time", ((threadCpuTimeNanosEnd - threadCpuTimeNanosStart) / 1000000.0) + "ms");
				*/
				System.exit(0);
			}
		}
		
		return checkIntent;
	}
	
	private Boolean checkIntent(Intent intent, IntentDataset intentDataset, MalformedIntentHandler handler)
	{
		IntentData intentData;
		
		String intentKey = (intent.getAction() != null)?intent.getAction():"";
		if(intent.getComponent() != null)
		{
			intentKey += intent.getComponent().getPackageName() + "/" + intent.getComponent().getClassName();
		}

		intentData = intentDataset.getIntent(intentKey);
		
		if(intentData == null)
		{
			throw new MalformedIntentException("Couldn't find Intent Key on described definition. " + "Actual Intent Key : " + intentKey + ".", 100);
		}
		
		
		//action
		if(intentData.getAction() != null)
		{
			if(intent.getAction() == null)
			{
				throw new MalformedIntentException("Couldn't match described Action : " + intentData.getAction() + ". Actual Action : " + "null.", 101);
			}
			else if(intentData.getAction().compareTo(intent.getAction()) != 0)
			{
				throw new MalformedIntentException("Couldn't match described Action : " + intentData.getAction() + ". Actual Action : " + intent.getAction() + ".", 101);
			}
			//return false;
		}

		//data
		if(intentData.getData() != null)
		{
			if(intent.getData() == null)
			{
				throw new MalformedIntentException("Couldn't match described Data : non-null. " + "Actual Data : null.", 102);
			}
			//return false;
		}
		
		//category
		if(intentData.lengthCategory() != 0)
		{
			if(intent.getCategories() == null)
			{
				Object[] ob1 = intentData.getCategoryArray();
				String intentData1 = ob1[0].toString();
				
				for(int i = 1; i < ob1.length; i++)
				{
					intentData1 += ", " + ob1[i].toString();
				}
				
				throw new MalformedIntentException("Couldn't match described Category : " + intentData1 + ".  Actual Category : null.", 104);
			}
			else if(intentData.lengthCategory() != intent.getCategories().size())
			{
				throw new MalformedIntentException("Couldn't match described number of Category : " + intentData.lengthCategory() + ".  Actual number of Category : " + intent.getCategories().size() + ".", 103);
				//return false;
			}
			else if(stringArrayCompare(intentData.getCategoryArray(), intent.getCategories().toArray()) == false)
			{
				Object[] ob1 = intentData.getCategoryArray();
				Object[] ob2 = intent.getCategories().toArray();
				
				String intentData1 = ob1[0].toString();
				String intentData2 = ob2[0].toString();
				
				for(int i = 1; i < ob1.length; i++)
				{
					intentData1 += ", " + ob1[i].toString();
					intentData2 += ", " + ob2[i].toString();
				}
				
				throw new MalformedIntentException("Couldn't match described Category : " + intentData1 + ".  Actual Category : " + intentData2 + ".", 104);
			}
			//return false;
		}
		
		//type
		if(intentData.getType() != null && intent.getType() == null)
		{
			throw new MalformedIntentException("Couldn't match described Type : non-null. " + "Actual Type : null.", 105);
			//return false;
		}
					
		//component package
		if(intentData.getComponentPkg() != null)
		{
			if (intent.getComponent() == null)
			{
				throw new MalformedIntentException("Couldn't match described Component : " + intentData.getComponentPkg() + "/" + intentData.getComponentCls() + ". " + "Actual Component : null.", 106);
			}
			else if(intentData.getComponentPkg().compareTo(intent.getComponent().getPackageName()) != 0)
			{
				throw new MalformedIntentException("Couldn't match described Component package : " + intentData.getComponentPkg() + ". Actual Component package : " + intent.getComponent().getPackageName() + ".", 106);
			}
			//return false;
		}
		
		//component class
		if(intentData.getComponentCls() != null)
		{
			if(intent.getComponent() == null)
			{
				throw new MalformedIntentException("Couldn't match described Component : " + intentData.getComponentPkg() + "/" + intentData.getComponentCls() + ". " + "Actual Component : null.", 107);
			}
			else if(intentData.getComponentCls().compareTo(intent.getComponent().getClassName()) != 0)
			{
				throw new MalformedIntentException("Couldn't match described Component class : " + intentData.getComponentCls() + ". Actual Component class : " + intent.getComponent().getClassName() + ".", 107);
			}
			//return false;
		}
		
		//extras
		if(intentData.lengthExtra() != 0)
		{
			if(intent.getExtras() == null)
			{
				Object[] keyTypePair = intentData.getExtraArray();
				
				String key = ((KeyTypePair)keyTypePair[0]).getKey();
				String type = ((KeyTypePair)keyTypePair[0]).getType();
				
				String keyAndTypes = key + "=" + type;
				
				for(int i = 1; i < keyTypePair.length; i++)
				{
					key = ((KeyTypePair)keyTypePair[i]).getKey();
					type = ((KeyTypePair)keyTypePair[i]).getType();

					keyAndTypes += ", " + key + "=" + type;
				}

				throw new MalformedIntentException("Couldn't match described Extra : " + keyAndTypes + ".  Actual Extra : null.", 109);
			}
			/*
			else if(intentData.lengthExtra() > intent.getExtras().size())
			{
				throw new MalformedIntentException("Couldn't match described number of Extra : " + intentData.lengthExtra() + ".  Actual number of Extra : " + intent.getExtras().size() + ".", 108);
				//return false;
			}
			*/
			
			if(extraArrayCompare(intentData.getExtraArray(), intent.getExtras()) == false)
			{
				
				Object[] keyTypePair = intentData.getExtraArray();
				Bundle bun = intent.getExtras();
				
				String key = ((KeyTypePair)keyTypePair[0]).getKey();
				String type = ((KeyTypePair)keyTypePair[0]).getType();
				
				String keyAndTypes = key + "=" + type;
				String RkeyAndTypes = key;
				
				Object ob = bun.get(key);

				if(ob == null)
				{
					RkeyAndTypes += "=null";
				}
				else
				{
					RkeyAndTypes += "=" + ob.getClass().getSimpleName();
				}
				
				for(int i = 1; i < keyTypePair.length; i++)
				{
					key = ((KeyTypePair)keyTypePair[i]).getKey();
					type = ((KeyTypePair)keyTypePair[i]).getType();

					keyAndTypes += ", " + key + "=" + type;
					
					ob = bun.get(key);
					
					if(ob == null)
					{
						RkeyAndTypes += ", " + key + "=null";
					}
					else
					{
						RkeyAndTypes += ", " + key + "=" + ob.getClass().getSimpleName();
					}
				}

				throw new MalformedIntentException("Couldn't match described Extra : " + keyAndTypes + ".  Actual Extra : " + RkeyAndTypes + ".", 109);
				//return false;
			}
		}
		
		//flag
		if(intentData.getFlag() != null && intent.getFlags() == 0)
		{
			throw new MalformedIntentException("Couldn't match described Flag : non-null. " + "Actual Flag : 0.", 110);
			//return false;
		}
		
		return true;
	}
	
	private Boolean stringArrayCompare(Object[] str1, Object[] str2)
	{
		ArrayList<String> strArr1 = new ArrayList<String>();
		ArrayList<String> strArr2 = new ArrayList<String>();
		
		for(int i = 0; i < str1.length; i++)
		{
			strArr1.add(str1[i].toString());
			strArr2.add(str2[i].toString());
		}
		
		Collections.sort(strArr1);
		Collections.sort(strArr2);
		
		for(int i = 0; i < str1.length; i++)
		{
			if(strArr1.get(i).compareTo(strArr2.get(i)) != 0)
			{
				return false;
			}
		}
		
		return true;
	}
	
	private Boolean extraArrayCompare(Object[] keyTypePair, Bundle bun)
	{
		for(int i = 0; i < keyTypePair.length; i++)
		{
			String key = ((KeyTypePair)keyTypePair[i]).getKey();
			String type = ((KeyTypePair)keyTypePair[i]).getType();
			
			Object ob = bun.get(key);
			
			if(ob == null)
			{
				return false;
			}
			
			if(checkTheSameClass(type, ob.getClass().getSimpleName()) == false)
			{
				return false;
			}
		}
		
		return true;
	}
	
	public boolean checkTheSameClass(String type, String className) 
	{
		type = type.replaceAll("\\[\\]", "\\\\\\[\\\\\\]");
		
		Pattern patt = Pattern.compile("^" + type + "$");
		Matcher m = patt.matcher(className);
		return m.matches();
	}
	
	private class StaticParser <T>
	{
		IntentDataset intentDataset = new IntentDataset();
		IntentData intentData;
		MalformedIntentException mexp;
		
		public ArrayList<Tuple<T>> pReturn(T a, String inp)
		{
			Tuple<T> tuple = new Tuple<T>();
			tuple.setTuple(a, inp);
			ArrayList<Tuple<T>> tupleList = new ArrayList<Tuple<T>>();
			tupleList.add(tuple);
			
			return tupleList;
		}
		
		public ArrayList<Tuple<T>> failure()
		{
			ArrayList<Tuple<T>> tupleList = new ArrayList<Tuple<T>>();
			return tupleList;
		}
		
		public ArrayList<Tuple<Character>> item(String inp)
		{
			ArrayList<Tuple<Character>> tupleList = new ArrayList<Tuple<Character>>();
			
			if(inp.length() == 0)
			{
				return tupleList;
			}
			else if(inp.length() > 0)
			{
				Tuple<Character> tuple = new Tuple<Character>();
				tuple.setTuple(inp.charAt(0), inp.substring(1, inp.length()));
				tupleList.add(tuple);
				
				return tupleList;
			}
			
			return null;
		}
		
		public ArrayList<Tuple<Character>> digit(String inp)
		{
			ArrayList<Tuple<Character>> tuplelist;
			
			StaticParser<Character> sp = new StaticParser<Character>();
			
			tuplelist = item(inp);
			if(tuplelist.isEmpty() == false && Character.isDigit(tuplelist.get(0).getValue()))
			{
				return sp.pReturn(tuplelist.get(0).getValue(), tuplelist.get(0).getInp());
			}
			
			return sp.failure();
		}
		
		public ArrayList<Tuple<Character>> lower(String inp)
		{
			ArrayList<Tuple<Character>> tuplelist;
			
			StaticParser<Character> sp = new StaticParser<Character>();
			
			tuplelist = item(inp);
			if(tuplelist.isEmpty() == false && Character.isLowerCase(tuplelist.get(0).getValue()))
			{
				return sp.pReturn(tuplelist.get(0).getValue(), tuplelist.get(0).getInp());
			}
			
			return sp.failure();
		}
		
		public ArrayList<Tuple<Character>> upper(String inp)
		{
			ArrayList<Tuple<Character>> tuplelist;
			
			StaticParser<Character> sp = new StaticParser<Character>();
			
			tuplelist = item(inp);
			if(tuplelist.isEmpty() == false && Character.isUpperCase(tuplelist.get(0).getValue()))
			{
				return sp.pReturn(tuplelist.get(0).getValue(), tuplelist.get(0).getInp());
			}
			
			return sp.failure();
		}
		
		public ArrayList<Tuple<Character>> letter(String inp)
		{
			ArrayList<Tuple<Character>> tuplelist;
			
			StaticParser<Character> sp = new StaticParser<Character>();
			
			tuplelist = item(inp);
			if(tuplelist.isEmpty() == false && Character.isLetter(tuplelist.get(0).getValue()))
			{
				return sp.pReturn(tuplelist.get(0).getValue(), tuplelist.get(0).getInp());
			}
			
			return sp.failure();
		}
		
		public ArrayList<Tuple<Character>> alphanum(String inp)
		{
			ArrayList<Tuple<Character>> tuplelist;
			
			StaticParser<Character> sp = new StaticParser<Character>();
			
			tuplelist = item(inp);
			if(tuplelist.isEmpty() == false && Character.isLetterOrDigit(tuplelist.get(0).getValue()))
			{
				return sp.pReturn(tuplelist.get(0).getValue(), tuplelist.get(0).getInp());
			}
			
			return sp.failure();
		}

		public ArrayList<Tuple<Character>> pChar(char c, String inp)
		{
			ArrayList<Tuple<Character>> tuplelist;
			
			StaticParser<Character> sp = new StaticParser<Character>();
			
			tuplelist = item(inp);
			if(tuplelist.isEmpty() == false && c == tuplelist.get(0).getValue())
			{
				return sp.pReturn(tuplelist.get(0).getValue(), tuplelist.get(0).getInp());
			}
			
			return sp.failure();
		}
		
		public ArrayList<Tuple<String>> pString(String x, String inp)
		{
			ArrayList<Tuple<String>> sTuplelist;
			
			ArrayList<Tuple<Character>> cTuplelist;
			
			StaticParser<String> sp = new StaticParser<String>();
			
			if(x.length() == 0)
			{
				return sp.pReturn("", inp);
			}
			else
			{
				cTuplelist = pChar(x.charAt(0), inp);
				if(cTuplelist.isEmpty())
				{
					return sp.failure();
				}
				
				sTuplelist = pString(x.substring(1, x.length()), cTuplelist.get(0).getInp());
				if(sTuplelist.isEmpty())
				{
					return sp.failure();
				}
				
				return sp.pReturn(cTuplelist.get(0).getValue() + sTuplelist.get(0).getValue(), sTuplelist.get(0).getInp());
			}
		}
		
		public ArrayList<Tuple<String>> ident(String inp)
		{
			ArrayList<Tuple<String>> sTuplelist;
			ArrayList<Tuple<Character>> cTuplelist;
			
			StaticParser<String> sp = new StaticParser<String>();
			
			cTuplelist = lower(inp);
			if(cTuplelist.isEmpty())
			{
				return sp.failure();
			}
			
			sTuplelist = manyAlphanum(cTuplelist.get(0).getInp());
			if(sTuplelist.isEmpty())
			{
				//return sp.failure();
				return sp.pReturn(String.valueOf(cTuplelist.get(0).getValue()), cTuplelist.get(0).getInp());
			}
			else
			{
				return sp.pReturn(cTuplelist.get(0).getValue() + sTuplelist.get(0).getValue(), sTuplelist.get(0).getInp());
			}
		}
		
		public ArrayList<Tuple<String>> manyAlphanum(String inp)
		{
			ArrayList<Tuple<String>> sTupleList;
			ArrayList<Tuple<Character>> cTuplelist;
			
			StaticParser<String> sp = new StaticParser<String>();
			
			cTuplelist = alphanum(inp);
			if(cTuplelist.isEmpty())
			{
				return sp.failure();
			}
			
			sTupleList = manyAlphanum(cTuplelist.get(0).inp);
			if(sTupleList.isEmpty())
			{
				return sp.pReturn(cTuplelist.get(0).getValue() + "", cTuplelist.get(0).inp);
			}
			else
			{
				return sp.pReturn(cTuplelist.get(0).getValue() + sTupleList.get(0).getValue(), sTupleList.get(0).inp);
			}
		}
		
		public ArrayList<Tuple<Integer>> nat(String inp)
		{
			ArrayList<Tuple<String>> sTuplelist;
			
			StaticParser<Integer> sp = new StaticParser<Integer>();
			
			sTuplelist = manyDigit(inp);
			if(sTuplelist.isEmpty())
			{
				return sp.failure();
			}
			else
			{
				return sp.pReturn(Integer.valueOf(sTuplelist.get(0).getValue()), sTuplelist.get(0).getInp());
			}
			
			
		}
		
		public ArrayList<Tuple<String>> manyDigit(String inp)
		{
			ArrayList<Tuple<String>> sTupleList;
			ArrayList<Tuple<Character>> cTuplelist;
			
			StaticParser<String> sp = new StaticParser<String>();
			
			cTuplelist = digit(inp);
			if(cTuplelist.isEmpty())
			{
				return sp.failure();
			}
			
			sTupleList = manyDigit(cTuplelist.get(0).inp);
			if(sTupleList.isEmpty())
			{
				return sp.pReturn(cTuplelist.get(0).getValue() + "", cTuplelist.get(0).inp);
			}
			else
			{
				return sp.pReturn(cTuplelist.get(0).getValue() + sTupleList.get(0).getValue(), sTupleList.get(0).inp);
			}
		}
		
		
		public ArrayList<Tuple<Integer>> pInt(String inp)
		{
			ArrayList<Tuple<Integer>> iTuplelist;
			ArrayList<Tuple<Character>> cTuplelist;
			
			StaticParser<Integer> sp = new StaticParser<Integer>();
			
			cTuplelist = pChar('-', inp);
			if(cTuplelist.isEmpty())
			{
				return nat(inp);
			}
			
			iTuplelist = nat(cTuplelist.get(0).getInp());
			if(iTuplelist.isEmpty())
			{
				return nat(inp);
			}
			else
			{
				return sp.pReturn((-iTuplelist.get(0).getValue()), iTuplelist.get(0).getInp());
			}
		}
	
		
		public ArrayList<Tuple<String>> space(String inp)
		{
			ArrayList<Tuple<String>> sTuplelist;
			
			StaticParser<String> sp = new StaticParser<String>();
			
			sTuplelist = manyisSpace(inp);
			
			return sTuplelist;
		}
		
		public ArrayList<Tuple<String>> manyisSpace(String inp)
		{
			ArrayList<Tuple<String>> sTupleList;
			ArrayList<Tuple<Character>> cTuplelist;
			
			StaticParser<String> sp = new StaticParser<String>();
			
			cTuplelist = item(inp);
			if(cTuplelist.isEmpty() || ' ' != cTuplelist.get(0).getValue())
			{
				return sp.pReturn(null, inp);
			}
			
			sTupleList = manyisSpace(cTuplelist.get(0).inp);
			return sp.pReturn(null, sTupleList.get(0).inp);
		}
		
		public ArrayList<Tuple<String>> identifier(String inp)
		{
			ArrayList<Tuple<String>> tmp1Tuplelist;
			ArrayList<Tuple<String>> tmp2Tuplelist;
			ArrayList<Tuple<String>> sTuplelist;
			
			StaticParser<String> sp = new StaticParser<String>();
			
			tmp1Tuplelist = sp.space(inp);
			sTuplelist = sp.ident(tmp1Tuplelist.get(0).getInp());
			if(sTuplelist.isEmpty())
			{
				return sp.failure();
			}
			tmp2Tuplelist = sp.space(sTuplelist.get(0).getInp());
			
			return sp.pReturn(sTuplelist.get(0).getValue(), tmp2Tuplelist.get(0).getInp());
		}
		
		public ArrayList<Tuple<Integer>> natural(String inp)
		{
			ArrayList<Tuple<String>> tmp1Tuplelist;
			ArrayList<Tuple<String>> tmp2Tuplelist;
			ArrayList<Tuple<Integer>> iTuplelist;
			
			StaticParser<Integer> sp = new StaticParser<Integer>();
			
			tmp1Tuplelist = sp.space(inp);
			iTuplelist = sp.nat(tmp1Tuplelist.get(0).getInp());
			if(iTuplelist.isEmpty())
			{
				return sp.failure();
			}
			tmp2Tuplelist = sp.space(iTuplelist.get(0).getInp());
			
			return sp.pReturn(iTuplelist.get(0).getValue(), tmp2Tuplelist.get(0).getInp());
		}
		
		public ArrayList<Tuple<Integer>> integer(String inp)
		{
			ArrayList<Tuple<String>> tmp1Tuplelist;
			ArrayList<Tuple<String>> tmp2Tuplelist;
			ArrayList<Tuple<Integer>> iTuplelist;
			
			StaticParser<Integer> sp = new StaticParser<Integer>();
			
			tmp1Tuplelist = sp.space(inp);
			iTuplelist = sp.pInt(tmp1Tuplelist.get(0).getInp());
			if(iTuplelist.isEmpty())
			{
				return sp.failure();
			}
			tmp2Tuplelist = sp.space(iTuplelist.get(0).getInp());
			
			return sp.pReturn(iTuplelist.get(0).getValue(), tmp2Tuplelist.get(0).getInp());
		}
		
		public ArrayList<Tuple<String>> symbol(String str, String inp)
		{
			ArrayList<Tuple<String>> tmp1Tuplelist;
			ArrayList<Tuple<String>> tmp2Tuplelist;
			ArrayList<Tuple<String>> sTuplelist;
			
			StaticParser<String> sp = new StaticParser<String>();
			
			tmp1Tuplelist = sp.space(inp);
			sTuplelist = sp.pString(str, tmp1Tuplelist.get(0).getInp());
			if(sTuplelist.isEmpty())
			{
				return sp.failure();
			}
			tmp2Tuplelist = sp.space(sTuplelist.get(0).getInp());
			
			return sp.pReturn(sTuplelist.get(0).getValue(), tmp2Tuplelist.get(0).getInp());
		}
		
		public ArrayList<Tuple<String>> identAlpha(String inp)
		{
			ArrayList<Tuple<String>> sTuplelist;
			ArrayList<Tuple<Character>> cTuplelist;
			
			StaticParser<String> sp = new StaticParser<String>();
			
			cTuplelist = sp.alphanum(inp);
			if(cTuplelist.isEmpty())
			{
				return sp.failure();
			}
			
			sTuplelist = manyAlphanum(cTuplelist.get(0).getInp());
			if(sTuplelist.isEmpty())
			{
				return sp.pReturn(String.valueOf(cTuplelist.get(0).getValue()), cTuplelist.get(0).getInp());
			}
			else
			{
				return sp.pReturn(cTuplelist.get(0).getValue() + sTuplelist.get(0).getValue(), sTuplelist.get(0).getInp());
			}
		}
		
		public ArrayList<Tuple<String>> idOrNum(String inp)
		{
			ArrayList<Tuple<String>> tmp1Tuplelist;
			ArrayList<Tuple<String>> tmp2Tuplelist;
			ArrayList<Tuple<String>> sTuplelist;
			
			StaticParser<String> sp = new StaticParser<String>();
			
			tmp1Tuplelist = sp.space(inp);
			sTuplelist = sp.identAlpha(tmp1Tuplelist.get(0).getInp());
			if(sTuplelist.isEmpty())
			{
				return sp.failure();
			}
			tmp2Tuplelist = sp.space(sTuplelist.get(0).getInp());
			
			return sp.pReturn(sTuplelist.get(0).getValue(), tmp2Tuplelist.get(0).getInp());
		}
		
		public ArrayList<Tuple<Character>> alphanumOrDot(String inp)
		{
			ArrayList<Tuple<Character>> cTuplelist;
			
			StaticParser<Character> sp = new StaticParser<Character>();
			
			cTuplelist = sp.item(inp);
			if(cTuplelist.isEmpty() == false && Character.isLetterOrDigit(cTuplelist.get(0).getValue()) == true)
			{
				return sp.pReturn(cTuplelist.get(0).getValue(), cTuplelist.get(0).getInp());
			}
			
			//cTuplelist = sp.item(inp);
			if(cTuplelist.isEmpty() == false && '.' == cTuplelist.get(0).getValue())
			{
				return sp.pReturn(cTuplelist.get(0).getValue(), cTuplelist.get(0).getInp());
			}
			
			if(cTuplelist.isEmpty() == false && '_' == cTuplelist.get(0).getValue())
			{
				return sp.pReturn(cTuplelist.get(0).getValue(), cTuplelist.get(0).getInp());
			}
			
			return sp.failure();
		}
		
		public ArrayList<Tuple<String>> identOrDot(String inp)
		{
			ArrayList<Tuple<String>> sTuplelist;
			ArrayList<Tuple<Character>> cTuplelist;
			
			StaticParser<String> sp = new StaticParser<String>();
			
			cTuplelist = sp.letter(inp);
			if(cTuplelist.isEmpty())
			{
				return sp.failure();
			}
			
			sTuplelist = manyAlphanumOrDot(cTuplelist.get(0).getInp());
			if(sTuplelist.isEmpty())
			{
				return sp.pReturn(String.valueOf(cTuplelist.get(0).getValue()), cTuplelist.get(0).getInp());
			}
			else
			{
				return sp.pReturn(cTuplelist.get(0).getValue() + sTuplelist.get(0).getValue(), sTuplelist.get(0).getInp());
			}
		}
		
		public ArrayList<Tuple<String>> manyAlphanumOrDot(String inp)
		{
			ArrayList<Tuple<String>> sTupleList;
			ArrayList<Tuple<Character>> cTuplelist;
			
			StaticParser<String> sp = new StaticParser<String>();
			
			cTuplelist = alphanumOrDot(inp);
			if(cTuplelist.isEmpty())
			{
				return sp.failure();
			}
			
			sTupleList = manyAlphanumOrDot(cTuplelist.get(0).inp);
			if(sTupleList.isEmpty())
			{
				return sp.pReturn(cTuplelist.get(0).getValue() + "", cTuplelist.get(0).inp);
			}
			else
			{
				return sp.pReturn(cTuplelist.get(0).getValue() + sTupleList.get(0).getValue(), sTupleList.get(0).inp);
			}
		}
		
		public ArrayList<Tuple<String>> idOrDot(String inp)
		{
			ArrayList<Tuple<String>> tmp1Tuplelist;
			ArrayList<Tuple<String>> tmp2Tuplelist;
			ArrayList<Tuple<String>> sTuplelist;
			
			StaticParser<String> sp = new StaticParser<String>();
			
			tmp1Tuplelist = sp.space(inp);
			sTuplelist = sp.identOrDot(tmp1Tuplelist.get(0).getInp());
			if(sTuplelist.isEmpty())
			{
				return sp.failure();
			}
			tmp2Tuplelist = sp.space(sTuplelist.get(0).getInp());
			
			return sp.pReturn(sTuplelist.get(0).getValue(), tmp2Tuplelist.get(0).getInp());
		}
		
		public ArrayList<Tuple<String>> intent(String inp)
		{
			ArrayList<Tuple<String>> tmpTuplelist;
			ArrayList<Tuple<String>> tmp2Tuplelist;
			ArrayList<Tuple<String>> s1Tuplelist;
			ArrayList<Tuple<String>> s2Tuplelist;
			
			StaticParser<String> sp = new StaticParser<String>();
			
			tmpTuplelist = symbol("{", inp);
			if(tmpTuplelist.isEmpty())
			{
				throw new MalformedIntentException("couldn't parse Intent. Token : " + errToken(inp), 0);
				//return sp.failure();
			}
			
			intentData = new IntentData();
			
			s1Tuplelist = fields(tmpTuplelist.get(0).getInp());
			if(s1Tuplelist.isEmpty())
			{
				return sp.failure();
			}
			
			tmpTuplelist = symbol("}", s1Tuplelist.get(0).getInp());
			if(tmpTuplelist.isEmpty())
			{
				throw mexp;
				//return sp.failure();
			}
			
			String intentKey = (intentData.getAction() != null)?intentData.getAction():"";
			if(intentData.getComponent() != null)
			{
				intentKey += intentData.getComponent();
			}
			
			if(intentDataset.getIntent(intentKey) != null)
			{
				throw new MalformedIntentException("Intent Key is not unique. Key : " + intentKey, 1);
			}
			
			intentDataset.addIntent(intentData);
			
			tmp2Tuplelist = symbol("||", tmpTuplelist.get(0).getInp());
			if(tmp2Tuplelist.isEmpty())
			{
				return sp.pReturn("{ " + s1Tuplelist.get(0).getValue() + " }", tmpTuplelist.get(0).getInp());
			}
			
			s2Tuplelist = intent(tmp2Tuplelist.get(0).getInp());
			if(s1Tuplelist.isEmpty())
			{
				throw new MalformedIntentException("couldn't parse Intent.", 0);
				//return sp.failure();
			}
			
			return sp.pReturn("{ " + s1Tuplelist.get(0).getValue() + " } || " + s2Tuplelist.get(0).getValue(), s2Tuplelist.get(0).getInp());
		}
		
		public ArrayList<Tuple<String>> fields(String inp)
		{
			ArrayList<Tuple<String>> tmpTuplelist;
			ArrayList<Tuple<String>> sTuplelist;
			ArrayList<Tuple<String>> fTuplelist;
			
			StaticParser<String> sp = new StaticParser<String>();
			
			sTuplelist = action(inp);
			if(sTuplelist.isEmpty() == false)
			{
				mexp = new MalformedIntentException("couldn't parse after Action field. Token : " + errToken(sTuplelist.get(0).getInp()), 16);
				fTuplelist = fields(sTuplelist.get(0).getInp());
				if(fTuplelist.isEmpty() == false)
				{
					return sp.pReturn(sTuplelist.get(0).getValue() + fTuplelist.get(0).getValue(), fTuplelist.get(0).getInp());
				}
			}
			
			sTuplelist = category(inp);
			if(sTuplelist.isEmpty() == false)
			{
				mexp = new MalformedIntentException("couldn't parse after Category field. Token : " + errToken(sTuplelist.get(0).getInp()), 18);
				fTuplelist = fields(sTuplelist.get(0).getInp());
				if(fTuplelist.isEmpty() == false)
				{
					return sp.pReturn(sTuplelist.get(0).getValue() + fTuplelist.get(0).getValue(), fTuplelist.get(0).getInp());
				}
			}
			
			sTuplelist = idata(inp);
			if(sTuplelist.isEmpty() == false)
			{
				mexp = new MalformedIntentException("couldn't parse after Data field. Token : " + errToken(sTuplelist.get(0).getInp()), 17);
				fTuplelist = fields(sTuplelist.get(0).getInp());
				if(fTuplelist.isEmpty() == false)
				{
					return sp.pReturn(sTuplelist.get(0).getValue() + fTuplelist.get(0).getValue(), fTuplelist.get(0).getInp());
				}
			}
			
			sTuplelist = itype(inp);
			if(sTuplelist.isEmpty() == false)
			{
				mexp = new MalformedIntentException("couldn't parse after Type field. Token : " + errToken(sTuplelist.get(0).getInp()), 19);
				fTuplelist = fields(sTuplelist.get(0).getInp());
				if(fTuplelist.isEmpty() == false)
				{
					return sp.pReturn(sTuplelist.get(0).getValue() + fTuplelist.get(0).getValue(), fTuplelist.get(0).getInp());
				}
			}
			
			sTuplelist = component(inp);
			if(sTuplelist.isEmpty() == false)
			{
				mexp = new MalformedIntentException("couldn't parse after Component field. Token : " + errToken(sTuplelist.get(0).getInp()), 20);
				fTuplelist = fields(sTuplelist.get(0).getInp());
				if(fTuplelist.isEmpty() == false)
				{
					return sp.pReturn(sTuplelist.get(0).getValue() + fTuplelist.get(0).getValue(), fTuplelist.get(0).getInp());
				}
			}
			
			sTuplelist = extra(inp);
			if(sTuplelist.isEmpty() == false)
			{
				mexp = new MalformedIntentException("couldn't parse after Extra field. Token : " + errToken(sTuplelist.get(0).getInp()), 21);
				fTuplelist = fields(sTuplelist.get(0).getInp());
				if(fTuplelist.isEmpty() == false)
				{
					return sp.pReturn(sTuplelist.get(0).getValue() + fTuplelist.get(0).getValue(), fTuplelist.get(0).getInp());
				}
			}
			
			sTuplelist = flag(inp);
			if(sTuplelist.isEmpty() == false)
			{
				mexp = new MalformedIntentException("couldn't parse after Flag field. Token : " + errToken(sTuplelist.get(0).getInp()), 22);
				fTuplelist = fields(sTuplelist.get(0).getInp());
				
				if(fTuplelist.isEmpty() == false)
				{
					return sp.pReturn(sTuplelist.get(0).getValue() + fTuplelist.get(0).getValue(), fTuplelist.get(0).getInp());
				}
			}
			
			//e
			return sp.pReturn("", inp);
		}
		
		public String errToken(String inp)
		{
			StringTokenizer tokenizer = new StringTokenizer(inp, " ");
			if(tokenizer.hasMoreTokens())
			{ 
				return tokenizer.nextToken();
			}
			
			return "";
		}
		
		public ArrayList<Tuple<String>> action(String inp)
		{
			ArrayList<Tuple<String>> sTuplelist;
			
			StaticParser<String> sp = new StaticParser<String>();
			String errmsg;
			
			sTuplelist = symbol("act", inp);
			if(sTuplelist.isEmpty())
			{
				return sp.failure();
			}
			errmsg = errToken(sTuplelist.get(0).getInp());
			
			sTuplelist = symbol("=", sTuplelist.get(0).getInp());
			if(sTuplelist.isEmpty())
			{
				throw new MalformedIntentException("couldn't parse Action field. Token : " + errmsg, 9);
				//return sp.failure();
			}
			errmsg = errToken(sTuplelist.get(0).getInp());
			
			sTuplelist = idOrDot(sTuplelist.get(0).getInp());
			if(sTuplelist.isEmpty())
			{
				throw new MalformedIntentException("couldn't parse Action field. Token : " + errmsg, 9);
				//return sp.failure();
			}
			
			if(intentData.getAction() != null)
			{
				throw new MalformedIntentException("Duplicated Action field.", 2);
			}
			intentData.setAction(sTuplelist.get(0).getValue());
			return sp.pReturn("act=" + sTuplelist.get(0).getValue() + " ", sTuplelist.get(0).getInp());
		}
		
		public ArrayList<Tuple<String>> category(String inp)
		{
			ArrayList<Tuple<String>> tmpTuplelist;
			ArrayList<Tuple<String>> s1Tuplelist;
			ArrayList<Tuple<String>> s2Tuplelist;
			
			StaticParser<String> sp = new StaticParser<String>();
			String errmsg;
			
			tmpTuplelist = symbol("cat", inp);
			if(tmpTuplelist.isEmpty())
			{
				return sp.failure();
			}
			errmsg = errToken(tmpTuplelist.get(0).getInp());
			
			tmpTuplelist = symbol("=", tmpTuplelist.get(0).getInp());
			if(tmpTuplelist.isEmpty())
			{
				throw new MalformedIntentException("couldn't parse Category field. Token : " + errmsg, 11);
				//return sp.failure();
			}
			errmsg = errToken(tmpTuplelist.get(0).getInp());
			
			tmpTuplelist = symbol("[", tmpTuplelist.get(0).getInp());
			if(tmpTuplelist.isEmpty())
			{
				throw new MalformedIntentException("couldn't parse Category field. Token : " + errmsg, 11);
				//return sp.failure();
			}
			errmsg = errToken(tmpTuplelist.get(0).getInp());
			
			s1Tuplelist = idOrDot(tmpTuplelist.get(0).getInp());
			if(s1Tuplelist.isEmpty())
			{
				throw new MalformedIntentException("couldn't parse Category field. Token : " + errmsg, 11);
				//return sp.failure();
			}
			
			if(intentData.lengthCategory() != 0)
			{
				throw new MalformedIntentException("Duplicated Category field.", 4);
			}
			intentData.addCategory(s1Tuplelist.get(0).getValue());
			
			s2Tuplelist = categorySub(s1Tuplelist.get(0).getInp());
			if(s2Tuplelist.isEmpty())
			{
				return sp.failure();
			}
			errmsg = errToken(s2Tuplelist.get(0).getInp());
			
			tmpTuplelist = symbol("]", s2Tuplelist.get(0).getInp());
			if(tmpTuplelist.isEmpty())
			{
				throw new MalformedIntentException("couldn't parse Category field. Token : " + errmsg, 11);
				//return sp.failure();
			}
			
			return sp.pReturn("cat=[" + s1Tuplelist.get(0).getValue() + s2Tuplelist.get(0).getValue() + "] ", tmpTuplelist.get(0).getInp());
		}
		
		public ArrayList<Tuple<String>> categorySub(String inp)
		{
			ArrayList<Tuple<String>> tmpTuplelist;
			ArrayList<Tuple<String>> s1Tuplelist;
			ArrayList<Tuple<String>> s2Tuplelist;
			
			StaticParser<String> sp = new StaticParser<String>();
			
			tmpTuplelist = symbol(",", inp);
			if(tmpTuplelist.isEmpty())
			{
				return sp.pReturn("", inp);
			}
			
			s1Tuplelist = idOrDot(tmpTuplelist.get(0).getInp());
			if(s1Tuplelist.isEmpty())
			{
				return sp.pReturn("", inp);
			}
			
			intentData.addCategory(s1Tuplelist.get(0).getValue());
			
			s2Tuplelist = categorySub(s1Tuplelist.get(0).getInp());
			
			return sp.pReturn(", " + s1Tuplelist.get(0).getValue() + s2Tuplelist.get(0).getValue(), s2Tuplelist.get(0).getInp());
		}
		
		public ArrayList<Tuple<String>> idata(String inp)
		{
			ArrayList<Tuple<String>> sTuplelist;
			
			StaticParser<String> sp = new StaticParser<String>();
			String errmsg;
			
			sTuplelist = symbol("dat", inp);
			if(sTuplelist.isEmpty())
			{
				return sp.failure();
			}
			errmsg = errToken(sTuplelist.get(0).getInp());
			
			sTuplelist = symbol("=", sTuplelist.get(0).getInp());
			if(sTuplelist.isEmpty())
			{
				throw new MalformedIntentException("couldn't parse Data field. Token : " + errmsg, 10);
				//return sp.failure();
			}
			errmsg = errToken(sTuplelist.get(0).getInp());
			
			sTuplelist = symbol("non-null", sTuplelist.get(0).getInp());
			if(sTuplelist.isEmpty())
			{
				throw new MalformedIntentException("couldn't parse Data field. Token : " + errmsg, 10);
				//return sp.failure();
			}
			
			if(intentData.getData() != null)
			{
				throw new MalformedIntentException("Duplicated Data field.", 3);
			}
			
			intentData.setData(true);
			
			return sp.pReturn("dat=" + sTuplelist.get(0).getValue() + " ", sTuplelist.get(0).getInp());
		}
		
		public ArrayList<Tuple<String>> itype(String inp)
		{
			ArrayList<Tuple<String>> sTuplelist;
			
			StaticParser<String> sp = new StaticParser<String>();
			String errmsg;
			
			sTuplelist = symbol("typ", inp);
			if(sTuplelist.isEmpty())
			{
				return sp.failure();
			}
			errmsg = errToken(sTuplelist.get(0).getInp());
			
			sTuplelist = symbol("=", sTuplelist.get(0).getInp());
			if(sTuplelist.isEmpty())
			{
				throw new MalformedIntentException("couldn't parse Type field. Token : " + errmsg, 12);
				//return sp.failure();
			}
			errmsg = errToken(sTuplelist.get(0).getInp());
			
			sTuplelist = symbol("non-null", sTuplelist.get(0).getInp());
			if(sTuplelist.isEmpty())
			{
				throw new MalformedIntentException("couldn't parse Type field. Token : " + errmsg, 12);
				//return sp.failure();
			}
			
			if(intentData.getType() != null)
			{
				throw new MalformedIntentException("Duplicated Type field.", 5);
			}
			intentData.setType(true);
			
			return sp.pReturn("typ=" + sTuplelist.get(0).getValue() + " ", sTuplelist.get(0).getInp());
		}
		
		public ArrayList<Tuple<String>> component(String inp)
		{
			ArrayList<Tuple<String>> tmpTuplelist;
			ArrayList<Tuple<String>> s1Tuplelist;
			ArrayList<Tuple<String>> s2Tuplelist;
			
			StaticParser<String> sp = new StaticParser<String>();
			String errmsg;
			
			tmpTuplelist = symbol("cmp", inp);
			if(tmpTuplelist.isEmpty())
			{
				return sp.failure();
			}
			errmsg = errToken(tmpTuplelist.get(0).getInp());
			
			tmpTuplelist = symbol("=", tmpTuplelist.get(0).getInp());
			if(tmpTuplelist.isEmpty())
			{
				throw new MalformedIntentException("couldn't parse Component field. Token : " + errmsg, 13);
				//return sp.failure();
			}
			errmsg = errToken(tmpTuplelist.get(0).getInp());
			
			s1Tuplelist = idOrDot(tmpTuplelist.get(0).getInp());
			if(s1Tuplelist.isEmpty())
			{
				throw new MalformedIntentException("couldn't parse Component field. Token : " + errmsg, 13);
				//return sp.failure();
			}
			errmsg = errToken(s1Tuplelist.get(0).getInp());
			
			tmpTuplelist = symbol("/", s1Tuplelist.get(0).getInp());
			if(tmpTuplelist.isEmpty())
			{
				throw new MalformedIntentException("couldn't parse Component field. Token : " + errmsg, 13);
				//return sp.failure();
			}
			errmsg = errToken(tmpTuplelist.get(0).getInp());
			
			s2Tuplelist = idOrDot(tmpTuplelist.get(0).getInp());
			if(s2Tuplelist.isEmpty() == false)
			{
				if(intentData.getComponentPkg() != null || intentData.getComponentCls() != null)
				{
					throw new MalformedIntentException("Duplicated Component field.", 6);
				}
				
				intentData.setComponentPkg(s1Tuplelist.get(0).getValue());
				intentData.setComponentCls(s2Tuplelist.get(0).getValue());
				return sp.pReturn("cmp=" + s1Tuplelist.get(0).getValue() + "/" + s2Tuplelist.get(0).getValue() + " ", s2Tuplelist.get(0).getInp());
			}
			
			tmpTuplelist = symbol(".", tmpTuplelist.get(0).getInp());
			if(tmpTuplelist.isEmpty())
			{
				throw new MalformedIntentException("couldn't parse Component field. Token : " + errmsg, 13);
				//return sp.failure();
			}
			errmsg = errToken(tmpTuplelist.get(0).getInp());
			
			s2Tuplelist = idOrDot(tmpTuplelist.get(0).getInp());
			if(s2Tuplelist.isEmpty() == false)
			{
				
				if(intentData.getComponentPkg() != null || intentData.getComponentCls() != null)
				{
					throw new MalformedIntentException("Duplicated Component field.", 6);
				}
				
				intentData.setComponentPkg(s1Tuplelist.get(0).getValue());
				intentData.setComponentCls(s1Tuplelist.get(0).getValue() + "." + s2Tuplelist.get(0).getValue());
				return sp.pReturn("cmp=" + s1Tuplelist.get(0).getValue() + "/" + s1Tuplelist.get(0).getValue() + "." + s2Tuplelist.get(0).getValue() + " ", s2Tuplelist.get(0).getInp());
			}
			
			throw new MalformedIntentException("couldn't parse Component field. Token : " + errmsg, 13);
			//return sp.failure();
		}
		
		public ArrayList<Tuple<String>> extra(String inp)
		{
			ArrayList<Tuple<String>> tmpTuplelist;
			ArrayList<Tuple<String>> s1Tuplelist;
			ArrayList<Tuple<String>> sTypeTuplelist;
			ArrayList<Tuple<String>> s2Tuplelist;
			
			StaticParser<String> sp = new StaticParser<String>();
			String errmsg;
			
			tmpTuplelist = symbol("[", inp);
			if(tmpTuplelist.isEmpty())
			{
				return sp.failure();
			}
			errmsg = errToken(tmpTuplelist.get(0).getInp());
			
			s1Tuplelist = idOrDot(tmpTuplelist.get(0).getInp());
			if(s1Tuplelist.isEmpty())
			{
				throw new MalformedIntentException("couldn't parse Extra field. Token : " + errmsg, 14);
				//return sp.failure();
			}
			errmsg = errToken(s1Tuplelist.get(0).getInp());
			
			tmpTuplelist = symbol("=", s1Tuplelist.get(0).getInp());
			if(tmpTuplelist.isEmpty())
			{
				throw new MalformedIntentException("couldn't parse Extra field. Token : " + errmsg, 14);
				//return sp.failure();
			}
			errmsg = errToken(tmpTuplelist.get(0).getInp());
			
			sTypeTuplelist = idOrDot(tmpTuplelist.get(0).getInp());
			if(sTypeTuplelist.isEmpty())
			{
				throw new MalformedIntentException("couldn't parse Extra field. Token : " + errmsg, 14);
				//return sp.failure();
			}
			
			//tmpTuplelist = symbol("[]", sTypeTuplelist.get(0).getInp());
			tmpTuplelist = arr(sTypeTuplelist.get(0).getInp());
			if(tmpTuplelist.isEmpty() == false)
			{
				sTypeTuplelist.get(0).setTuple(sTypeTuplelist.get(0).getValue() + tmpTuplelist.get(0).getValue(), tmpTuplelist.get(0).getInp());
			}
			
			if(intentData.lengthExtra() != 0)
			{
				throw new MalformedIntentException("Duplicated Extra field.", 7);
			}
			intentData.addExtra(new KeyTypePair(s1Tuplelist.get(0).getValue(), sTypeTuplelist.get(0).getValue()));
			
			s2Tuplelist = extraSub(sTypeTuplelist.get(0).getInp());
			if(s2Tuplelist.isEmpty())
			{
				return sp.failure();
			}
			errmsg = errToken(s2Tuplelist.get(0).getInp());
			
			tmpTuplelist = symbol("]", s2Tuplelist.get(0).getInp());
			if(tmpTuplelist.isEmpty())
			{
				throw new MalformedIntentException("couldn't parse Extra field. Token : " + errmsg, 14);
				//return sp.failure();
			}
			
			return sp.pReturn("[" + s1Tuplelist.get(0).getValue() + "=" + sTypeTuplelist.get(0).getValue() + s2Tuplelist.get(0).getValue() + "] ", tmpTuplelist.get(0).getInp());
		}
		
		public ArrayList<Tuple<String>> extraSub(String inp)
		{
			ArrayList<Tuple<String>> tmpTuplelist;
			ArrayList<Tuple<String>> s1Tuplelist;
			ArrayList<Tuple<String>> sTypeTuplelist;
			ArrayList<Tuple<String>> s2Tuplelist;
			
			StaticParser<String> sp = new StaticParser<String>();
			String errmsg;
			
			tmpTuplelist = symbol(",", inp);
			if(tmpTuplelist.isEmpty())
			{
				return sp.pReturn("", inp);
			}
			errmsg = errToken(tmpTuplelist.get(0).getInp());
			
			s1Tuplelist = idOrDot(tmpTuplelist.get(0).getInp());
			if(s1Tuplelist.isEmpty())
			{
				throw new MalformedIntentException("couldn't parse Extra field. Token : " + errmsg, 14);
				//return sp.failure();
			}
			errmsg = errToken(s1Tuplelist.get(0).getInp());
			
			tmpTuplelist = symbol("=", s1Tuplelist.get(0).getInp());
			if(tmpTuplelist.isEmpty())
			{
				throw new MalformedIntentException("couldn't parse Extra field. Token : " + errmsg, 14);
				//return sp.failure();
			}
			errmsg = errToken(tmpTuplelist.get(0).getInp());
			
			sTypeTuplelist = idOrDot(tmpTuplelist.get(0).getInp());
			if(sTypeTuplelist.isEmpty())
			{
				throw new MalformedIntentException("couldn't parse Extra field. Token : " + errmsg, 14);
				//return sp.failure();
			}
			
			//tmpTuplelist = symbol("[]", sTypeTuplelist.get(0).getInp());
			tmpTuplelist = arr(sTypeTuplelist.get(0).getInp());
			if(tmpTuplelist.isEmpty() == false)
			{
				sTypeTuplelist.get(0).setTuple(sTypeTuplelist.get(0).getValue() + tmpTuplelist.get(0).getValue(), tmpTuplelist.get(0).getInp());
			}
			
			intentData.addExtra(new KeyTypePair(s1Tuplelist.get(0).getValue(), sTypeTuplelist.get(0).getValue()));
			
			s2Tuplelist = extraSub(sTypeTuplelist.get(0).getInp());
			
			return sp.pReturn(", " + s1Tuplelist.get(0).getValue() + s2Tuplelist.get(0).getValue(), s2Tuplelist.get(0).getInp());
		}
		
		public ArrayList<Tuple<String>> flag(String inp)
		{
			ArrayList<Tuple<String>> sTuplelist;
			
			StaticParser<String> sp = new StaticParser<String>();
			
			sTuplelist = symbol("flg", inp);
			if(sTuplelist.isEmpty())
			{
				return sp.failure();
			}
			
			if(intentData.getFlag() != null)
			{
				throw new MalformedIntentException("Duplicated Flag field.", 8);
			}
			intentData.setFlag(true);
			
			return sp.pReturn("flg ", sTuplelist.get(0).getInp());
		}
		
		public ArrayList<Tuple<String>> arr(String inp)
		{
			ArrayList<Tuple<String>> a1Tuplelist;
			ArrayList<Tuple<String>> a2Tuplelist;
			
			StaticParser<String> sp = new StaticParser<String>();
			
			a1Tuplelist = symbol("[]", inp);
			if(a1Tuplelist.isEmpty())
			{
				return sp.pReturn("", inp);
			}
			
			a2Tuplelist = arr(a1Tuplelist.get(0).getInp());
			
			return sp.pReturn(a1Tuplelist.get(0).getValue() + a2Tuplelist.get(0).getValue(), a2Tuplelist.get(0).getInp());
		}

		public IntentDataset parse(String inp)
		{
			ArrayList<Tuple<String>> sTuplelist;
			
			sTuplelist = intent(inp);
			
			if(sTuplelist.isEmpty())
			{
				return null;
			}
			
			if(sTuplelist.get(0).getInp().length() != 0)
			{
				return null;
			}
			
			return intentDataset;
		}
		
		public String exceptionIntentNumberMsg()
		{
			return "IntentNumber : " + intentDataset.length();
		}
		
	}
	
	public class Tuple <T> {
		private T value;
		private String inp; 
		
		public Tuple()
		{
		}
		
		public Tuple(T value, String inp)
		{
			this.value = value;
			this.inp = inp;
		}
		
		public void setTuple(T value, String inp)
		{
			this.value = value;
			this.inp = inp;
		}
		
		public T getValue()
		{
			return value;
		}
		
		public String getInp()
		{
			return inp;
		}
	}
	
	public class KeyTypePair{
		private String key;
		private String type; 
		
		public KeyTypePair()
		{
		}
		
		public KeyTypePair(String key, String type)
		{
			this.key = key;
			this.type = type;
		}
		
		public void setPair(String key, String type)
		{
			this.key = key;
			this.type = type;
		}
		
		public String getKey()
		{
			return key;
		}
		
		public String getType()
		{
			return type;
		}
	}
		
	public class IntentData{
		private String action;
		private ArrayList<String> category = new ArrayList<String>();
		private Boolean data;
		private Boolean type;
		private String componentPkg;
		private String componentCls;
		private ArrayList<KeyTypePair> extra = new ArrayList<KeyTypePair>();
		private Boolean flag;
		
		public void setAction(String action)
		{
			this.action = action;
		}
		
		public String getAction()
		{
			return action;
		}
		
		public void addCategory(String data)
		{
			category.add(data);
		}
		
		public String getCategory(int index)
		{
			return category.get(index);
		}
		
		public Object[] getCategoryArray()
		{
			return category.toArray();
		}
		
		public int lengthCategory()
		{
			return category.size();
		}
		
		public void setData(Boolean data)
		{
			this.data = data;
		}
		
		public Boolean getData()
		{
			return data;
		}
		
		public void setType(Boolean type)
		{
			this.type = type;
		}
		
		public Boolean getType()
		{
			return type;
		}
		
		public void setComponentPkg(String componentPkg)
		{
			this.componentPkg = componentPkg;
		}
		
		public String getComponentPkg()
		{
			return componentPkg;
		}

		public void setComponentCls(String componentCls)
		{
			this.componentCls = componentCls;
		}
		
		public String getComponentCls()
		{
			return componentCls;
		}
		
		public String getComponent()
		{
			if(componentPkg == null && componentCls == null)
			{
				return "";
			}
			else if(componentPkg == null)
			{
				return componentCls;
			}
			else if(componentCls == null)
			{
				return componentPkg;
			}
			else
			{
				return componentPkg + "/" + componentCls;
			}
		}
		
		public void addExtra(KeyTypePair extra)
		{
			this.extra.add(extra);
		}
		
		public KeyTypePair getExtra(int index)
		{
			return extra.get(index);
		}
		
		public Object[] getExtraArray()
		{
			return extra.toArray();
		}
		
		public int lengthExtra()
		{
			return extra.size();
		}
		
		public void setFlag(Boolean flag)
		{
			this.flag = flag;
		}
		
		public Boolean getFlag()
		{
			return flag;
		}
		
	}
	
	public class IntentDataset{
		private HashMap<String, IntentData> intentDataset = new HashMap<String, IntentData>();
		
		public void addIntent(IntentData intentdata)
		{
			String key = (intentdata.action != null)?intentdata.action : "";
			key += intentdata.getComponent();
			
			this.intentDataset.put(key, intentdata);
		}
		
		public IntentData getIntent(String ActionAndComponent)
		{
			return intentDataset.get(ActionAndComponent);
		}
		
		public void remove(String ActionAndComponent)
		{
			intentDataset.remove(ActionAndComponent);
		}
		
		public void removeAll()
		{
			intentDataset.clear();
		}
		
		public int length()
		{
			return intentDataset.size();
		}
	}
}
