package MyDataStructure;

import java.io.Serializable;

import utils.Point3D;

public class Vertex implements node_data, Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 221792218006334352L;
	private int _key;
	private Point3D _location;
	private double _weight;
	String _info;
	int _tag;
	public Vertex()
	{
		this._key = 0;
		this._location = null;
		this._weight = 0;
		this._info = "";
		this._tag = 0;
	}
	public Vertex(int key) 
	{
		this._key = key;
		this._location = null;
		this._weight = 0;
		this._info = "";
		this._tag = 0;
	}

	public Vertex(int key, Point3D location) 
	{ 
		this._key = key;
		this._location = new Point3D(location);
	}

	public Vertex(int key,Point3D location,double weight) 
	{
		this._key = key;
		if(location != null)
		{
			this._location = new Point3D(location);
		}
		else
		{
			this._location = null;
		}
		this._weight = weight;
		this._info = "";
	}
	public Vertex(int key,Point3D location,double weight,int tag) 
	{
		this._key = key;
		if(location != null)
		{
			this._location = new Point3D(location);
		}
		else
		{
			this._location = null;
		}
		this._weight = weight;
		this._tag = tag;
		this._info = "";
	}
	public Vertex(Vertex other)
	{
		this(other._key,other._location,other._weight,other._tag);
		this._info = other._info;

	}
	public Vertex(node_data other)
	{
		this(other.getKey(),other.getLocation(),other.getWeight(),other.getTag());
		this._info = other.getInfo();

	}


	@Override
	public int getKey() 
	{
		// TODO Auto-generated method stub
		return _key;
	}

	@Override
	public Point3D getLocation() 
	{
		// TODO Auto-generated method stub
		return _location;
	}

	@Override
	public void setLocation(Point3D p) 
	{
		// TODO Auto-generated method stub
		this._location = p; // by reference or should do copy?

	}

	@Override
	public double getWeight() {
		// TODO Auto-generated method stub
		return _weight;
	}

	@Override
	public void setWeight(double w) 
	{
		// TODO Auto-generated method stub
		this._weight = w;
	}

	@Override
	public String getInfo() {
		// TODO Auto-generated method stub
		return _info;
	}

	@Override
	public void setInfo(String s) 
	{
		// TODO Auto-generated method stub
		this._info = s;
	}

	@Override
	public int getTag() {
		// TODO Auto-generated method stub
		return _tag;
	}

	@Override
	public void setTag(int t) 
	{
		// TODO Auto-generated method stub
		this._tag = t;
	}


}
