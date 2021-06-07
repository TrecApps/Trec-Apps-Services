package com.trecapps.falsehoodsearch.repos;


import com.trecapps.falsehoodsearch.models.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigInteger;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

public class PublicFalsehoodRepository implements PublicFalsehoodRepo
{
	Set<PublicFalsehood> appeals = new TreeSet<>();
	
    private static List<PublicFalsehood> cullList(Pageable p, List<PublicFalsehood> f)
    {
        int number = p.getPageNumber();
        int size = p.getPageSize();

        int start = number * size;

        int end = start + size;
        return end < f.size() ? f.subList(start, end): f;
    }
    
    boolean foundTerm(String tagAtt, String terms)
    {
    	if(tagAtt == null || terms == null)
    		return true;
    	
    	List<String> from = List.of(tagAtt.split("\\|"));
    	
    	for(int rust = 0; rust < from.size(); rust++)
    	{
    		if(terms.indexOf(from.get(rust)) != -1)
    			return true;
    	}
    	return false;
    }


    @Override
    public List<PublicFalsehood> findAll() {
        return new ArrayList<PublicFalsehood>(appeals);
    }

    @Override
    public List<PublicFalsehood> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<PublicFalsehood> findAll(Pageable pageable) {
        int number = pageable.getPageNumber();
        int size = pageable.getPageSize();

        int start = number * size;

        int end = start + number;

        List<PublicFalsehood> preRet = new ArrayList<>(appeals);

        return new TrecPage<PublicFalsehood>( preRet.subList(start, end));
    }

    @Override
    public List<PublicFalsehood> findAllById(Iterable<BigInteger> bigIntegers) {
        return null;
    }

    @Override
    public long count() {
        return appeals.size();
    }

    @Override
    public void deleteById(BigInteger bigInteger) {
        List<PublicFalsehood> preRet = new ArrayList<>(appeals);
        for(int Rust = 0; Rust < preRet.size(); Rust++)
        {
            if(preRet.get(Rust).getId().equals(bigInteger))
            {
                preRet.remove(Rust);
                appeals = new TreeSet<>(preRet);
                return;
            }
        }
    }

    @Override
    public void delete(PublicFalsehood entity) {
        appeals.remove(entity);
    }

    @Override
    public void deleteAll(Iterable<? extends PublicFalsehood> entities) {
        for(PublicFalsehood u : entities)
        {
        	delete(u);
        }
    }

    @Override
    public void deleteAll() {
        appeals.clear();
    }

    @Override
    public <S extends PublicFalsehood> S save(S entity) {
    	if(entity == null)
            throw new IllegalArgumentException("Entity passed to Save Method must not be null");

        boolean add = entity.getId() == null;
        if(add)
        {
            BigInteger i = new BigInteger("0");

            for(PublicFalsehood app: appeals)
            {
                if(!i.equals(app.getId()))
                    entity.setId(i);
                i = i.add(BigInteger.ONE);
            }
            
            if(entity.getId() == null)
            	entity.setId(i);
        }

        appeals.removeIf((app) -> entity.getId().equals(app.getId()));
        appeals.add(entity);

        return entity;
    }

    @Override
    public <S extends PublicFalsehood> List<S> saveAll(Iterable<S> entities) {
        List<S> ret = new LinkedList<>();
        for(S s: entities)
        {
            ret.add(save(s));
        }
        return ret;
    }

    @Override
    public Optional<PublicFalsehood> findById(BigInteger bigInteger) {
        for(PublicFalsehood app: appeals)
        {
            if(bigInteger.equals(app.getId()))
                return Optional.of(app);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(BigInteger bigInteger) {
        for(PublicFalsehood app: appeals)
        {
            if(bigInteger.equals(app.getId()))
                return true;
        }
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends PublicFalsehood> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<PublicFalsehood> entities) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public PublicFalsehood getOne(BigInteger bigInteger) {
        for(PublicFalsehood app: appeals)
        {
            if(bigInteger.equals(app.getId()))
                return app;
        }

        return new PublicFalsehood();
    }

    @Override
    public <S extends PublicFalsehood> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends PublicFalsehood> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends PublicFalsehood> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends PublicFalsehood> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends PublicFalsehood> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends PublicFalsehood> boolean exists(Example<S> example) {
        return false;
    }

	@Override
	public List<PublicFalsehood> getSubmittedFalsehoods(Pageable p) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			if (status == 0 || status == 2)
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoods(Pageable p, byte offType) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			if ((offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetween(Pageable p, byte offType, Date begin, Date end) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		ret = cullList(p, ret);

		return ret;
	}


	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByOfficial(Pageable p, byte offType, Date begin, Date end,
			PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndBySeverity(Pageable p, byte offType, Date begin, Date end,
			byte maxSeverity, byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev >= maxSeverity && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndBySeverityOfficial(Pageable p, byte offType, Date begin, Date end,
			byte maxSeverity, byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev >= maxSeverity && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMinSeverity(Pageable p, byte offType, Date begin, Date end,
			byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMinSeverityOfficial(Pageable p, byte offType, Date begin, Date end,
			byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMaxSeverity(Pageable p, byte offType, Date begin, Date end,
			byte maxSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev >= maxSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficial(Pageable p, byte offType, Date begin, Date end,
			byte maxSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev >= maxSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	
	
	
	//// Now do Before
	
	
	
	
	
	
	
	
	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBefore(Pageable p, byte offType, Date end) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByOfficial(Pageable p, byte offType, Date end,
			PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11)	&& d.getTime() <= end.getTime() && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndBySeverity(Pageable p, byte offType, Date end,
			byte maxSeverity, byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev >= maxSeverity && sev <= minSeverity
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndBySeverityOfficial(Pageable p, byte offType, Date end,
			byte maxSeverity, byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev >= maxSeverity && sev <= minSeverity
					&& d.getTime() <= end.getTime() && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByMinSeverity(Pageable p, byte offType, Date end,
			byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev <= minSeverity
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByMinSeverityOfficial(Pageable p, byte offType, Date end,
			byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev <= minSeverity && d.getTime() <= end.getTime() && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByMaxSeverity(Pageable p, byte offType, Date end,
			byte maxSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev >= maxSeverity && d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByMaxSeverityOfficial(Pageable p, byte offType, Date end,
			byte maxSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev >= maxSeverity && d.getTime() <= end.getTime() && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	
	//// Now do No Dates
	
	
	
	
	
	
	
	
	

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByRegion(Pageable p, byte offType,   Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByInstitution(Pageable p, byte offType,  
			Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			if ((offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByOfficial(Pageable p, byte offType,  
			PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			if ((offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBySeverity(Pageable p, byte offType,  
			byte maxSeverity, byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if ((offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev >= maxSeverity && sev <= minSeverity)
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBySeverityOfficial(Pageable p, byte offType,  
			byte maxSeverity, byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if ((offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev >= maxSeverity && sev <= minSeverity && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByMinSeverity(Pageable p, byte offType,  
			byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if ((offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev <= minSeverity)
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByMinSeverityOfficial(Pageable p, byte offType,  
			byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if ((offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev <= minSeverity && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByMaxSeverity(Pageable p, byte offType,  
			byte maxSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if ((offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev >= maxSeverity)
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByMaxSeverityOfficial(Pageable p, byte offType,  
			byte maxSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if ((offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev >= maxSeverity && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	
	
	/////////// Rejected Falsehoods ///////////
	
	
	
	
	
	
	
	@Override
	public List<PublicFalsehood> getRejectedFalsehoods(Pageable p, byte offType) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			if ((offType > 15 || offType == pf.getOfficialType()) && status > 4)
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetween(Pageable p, byte offType, Date begin, Date end) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && status > 4 && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);

		}
		ret = cullList(p, ret);

		return ret;
	}



	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByOfficial(Pageable p, byte offType, Date begin, Date end,
			PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && status > 4 && d.getTime() >= begin.getTime() && official.equals(pf.getOfficial())
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndBySeverity(Pageable p, byte offType, Date begin, Date end,
			byte maxSeverity, byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev >= maxSeverity && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndBySeverityOfficial(Pageable p, byte offType, Date begin, Date end,
			byte maxSeverity, byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev >= maxSeverity && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByMinSeverity(Pageable p, byte offType, Date begin, Date end,
			byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByMinSeverityOfficial(Pageable p, byte offType, Date begin, Date end,
			byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByMaxSeverity(Pageable p, byte offType, Date begin, Date end,
			byte maxSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev >= maxSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByMaxSeverityOfficial(Pageable p, byte offType, Date begin, Date end,
			byte maxSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev >= maxSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	
	
	
	//// Now do Before
	
	
	
	
	
	
	
	
	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBefore(Pageable p, byte offType, Date end) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && status > 4 && d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByOfficial(Pageable p, byte offType, Date end,
			PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && status > 4	&& d.getTime() <= end.getTime() && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndBySeverity(Pageable p, byte offType, Date end,
			byte maxSeverity, byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev >= maxSeverity && sev <= minSeverity
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndBySeverityOfficial(Pageable p, byte offType, Date end,
			byte maxSeverity, byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev >= maxSeverity && sev <= minSeverity
					&& d.getTime() <= end.getTime() && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByMinSeverity(Pageable p, byte offType, Date end,
			byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev <= minSeverity
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByMinSeverityOfficial(Pageable p, byte offType, Date end,
			byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev <= minSeverity && d.getTime() <= end.getTime() && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByMaxSeverity(Pageable p, byte offType, Date end,
			byte maxSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev >= maxSeverity && d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByMaxSeverityOfficial(Pageable p, byte offType, Date end,
			byte maxSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev >= maxSeverity && d.getTime() <= end.getTime() && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	
	
	//// Now do No Dates
	
	
	
	
	
	
	
	
	

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByRegion(Pageable p, byte offType,   Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if ((offType > 15 || offType == pf.getOfficialType()) && status > 4 && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByInstitution(Pageable p, byte offType,  
			Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			if ((offType > 15 || offType == pf.getOfficialType()) && status > 4 && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByOfficial(Pageable p, byte offType,  
			PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			if ((offType > 15 || offType == pf.getOfficialType()) && status > 4 && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBySeverity(Pageable p, byte offType,  
			byte maxSeverity, byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if ((offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev >= maxSeverity && sev <= minSeverity)
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBySeverityOfficial(Pageable p, byte offType,  
			byte maxSeverity, byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if ((offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev >= maxSeverity && sev <= minSeverity && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByMinSeverity(Pageable p, byte offType,  
			byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if ((offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev <= minSeverity)
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByMinSeverityOfficial(Pageable p, byte offType,  
			byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if ((offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev <= minSeverity && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByMaxSeverity(Pageable p, byte offType,  
			byte maxSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if ((offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev >= maxSeverity)
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByMaxSeverityOfficial(Pageable p, byte offType,  
			byte maxSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if ((offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev >= maxSeverity && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	
	//////// Now Including search terms
	
	
	
	@Override
	public List<PublicFalsehood> getConfirmedFalsehoods(Pageable p, String terms, byte offType) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetween(Pageable p, String terms, byte offType, Date begin, Date end) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		ret = cullList(p, ret);

		return ret;
	}



	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByOfficial(Pageable p, String terms, byte offType, Date begin, Date end,
			PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndBySeverity(Pageable p, String terms, byte offType, Date begin, Date end,
			byte maxSeverity, byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev >= maxSeverity && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndBySeverityOfficial(Pageable p, String terms, byte offType, Date begin, Date end,
			byte maxSeverity, byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev >= maxSeverity && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMinSeverity(Pageable p, String terms, byte offType, Date begin, Date end,
			byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMinSeverityOfficial(Pageable p, String terms, byte offType, Date begin, Date end,
			byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMaxSeverity(Pageable p, String terms, byte offType, Date begin, Date end,
			byte maxSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev >= maxSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficial(Pageable p, String terms, byte offType, Date begin, Date end,
			byte maxSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev >= maxSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}
	
	
	//// Now do Before

	
	
	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBefore(Pageable p, String terms, byte offType, Date end) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByOfficial(Pageable p, String terms, byte offType, Date end,
			PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11)	&& d.getTime() <= end.getTime() && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndBySeverity(Pageable p, String terms, byte offType, Date end,
			byte maxSeverity, byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev >= maxSeverity && sev <= minSeverity
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndBySeverityOfficial(Pageable p, String terms, byte offType, Date end,
			byte maxSeverity, byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev >= maxSeverity && sev <= minSeverity
					&& d.getTime() <= end.getTime() && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByMinSeverity(Pageable p, String terms, byte offType, Date end,
			byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev <= minSeverity
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByMinSeverityOfficial(Pageable p, String terms, byte offType, Date end,
			byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev <= minSeverity && d.getTime() <= end.getTime() && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByMaxSeverity(Pageable p, String terms, byte offType, Date end,
			byte maxSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev >= maxSeverity && d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByMaxSeverityOfficial(Pageable p, String terms, byte offType, Date end,
			byte maxSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev >= maxSeverity && d.getTime() <= end.getTime() && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	
	//// Now do No Dates
	
	
	
	
	
	
	
	


	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByOfficial(Pageable p, String terms, byte offType,  
			PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBySeverity(Pageable p, String terms, byte offType,  
			byte maxSeverity, byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev >= maxSeverity && sev <= minSeverity)
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBySeverityOfficial(Pageable p, String terms, byte offType,  
			byte maxSeverity, byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev >= maxSeverity && sev <= minSeverity && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByMinSeverity(Pageable p, String terms, byte offType,  
			byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev <= minSeverity)
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByMinSeverityOfficial(Pageable p, String terms, byte offType,  
			byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev <= minSeverity && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByMaxSeverity(Pageable p, String terms, byte offType,  
			byte maxSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev >= maxSeverity)
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByMaxSeverityOfficial(Pageable p, String terms, byte offType,  
			byte maxSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && ((status > 1 && status < 5) || status > 11) && sev >= maxSeverity && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	/////////// Rejected Falsehoods ///////////
	
	
	
	
	
	
	
	@Override
	public List<PublicFalsehood> getRejectedFalsehoods(Pageable p, String terms, byte offType) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && status > 4)
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetween(Pageable p, String terms, byte offType, Date begin, Date end) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && status > 4 && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		ret = cullList(p, ret);

		return ret;
	}



	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByOfficial(Pageable p, String terms, byte offType, Date begin, Date end,
			PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && status > 4 && d.getTime() >= begin.getTime() && official.equals(pf.getOfficial())
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndBySeverity(Pageable p, String terms, byte offType, Date begin, Date end,
			byte maxSeverity, byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev >= maxSeverity && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndBySeverityOfficial(Pageable p, String terms, byte offType, Date begin, Date end,
			byte maxSeverity, byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev >= maxSeverity && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByMinSeverity(Pageable p, String terms, byte offType, Date begin, Date end,
			byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByMinSeverityOfficial(Pageable p, String terms, byte offType, Date begin, Date end,
			byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByMaxSeverity(Pageable p, String terms, byte offType, Date begin, Date end,
			byte maxSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev >= maxSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByMaxSeverityOfficial(Pageable p, String terms, byte offType, Date begin, Date end,
			byte maxSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev >= maxSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	
	//// Now do Before
	
	
	
	
	
	
	
	
	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBefore(Pageable p, String terms, byte offType, Date end) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && status > 4 && d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByOfficial(Pageable p, String terms, byte offType, Date end,
			PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && status > 4	&& d.getTime() <= end.getTime() && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndBySeverity(Pageable p, String terms, byte offType, Date end,
			byte maxSeverity, byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev >= maxSeverity && sev <= minSeverity
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndBySeverityOfficial(Pageable p, String terms, byte offType, Date end,
			byte maxSeverity, byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev >= maxSeverity && sev <= minSeverity
					&& d.getTime() <= end.getTime() && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByMinSeverity(Pageable p, String terms, byte offType, Date end,
			byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev <= minSeverity
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByMinSeverityOfficial(Pageable p, String terms, byte offType, Date end,
			byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev <= minSeverity && d.getTime() <= end.getTime() && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByMaxSeverity(Pageable p, String terms, byte offType, Date end,
			byte maxSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev >= maxSeverity && d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByMaxSeverityOfficial(Pageable p, String terms, byte offType, Date end,
			byte maxSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev >= maxSeverity && d.getTime() <= end.getTime() && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	
	//// Now do No Dates
	
	




	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByOfficial(Pageable p, String terms, byte offType,  
			PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && status > 4 && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}


	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBySeverity(Pageable p, String terms, byte offType,  
			byte maxSeverity, byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev >= maxSeverity && sev <= minSeverity)
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBySeverityOfficial(Pageable p, String terms, byte offType,  
			byte maxSeverity, byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev >= maxSeverity && sev <= minSeverity && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByMinSeverity(Pageable p, String terms, byte offType,  
			byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev <= minSeverity)
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByMinSeverityOfficial(Pageable p, String terms, byte offType,  
			byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev <= minSeverity && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByMaxSeverity(Pageable p, String terms, byte offType,  
			byte maxSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev >= maxSeverity)
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByMaxSeverityOfficial(Pageable p, String terms, byte offType,  
			byte maxSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if (foundTerm(pf.getTags(), terms) && (offType > 15 || offType == pf.getOfficialType()) && status > 4 && sev >= maxSeverity && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}



}
