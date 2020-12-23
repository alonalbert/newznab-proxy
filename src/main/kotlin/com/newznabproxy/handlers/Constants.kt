val CAPS = """<?xml version="1.0"?>
<caps>
  <server appversion="1.0.0" version="0.1" title="NZBgeek" strapline="" email="info@nzbgeek.info" url="https://api.nzbgeek.info/" image="https://cdn.nzbgeek.info/covers/nzbgeek.png"/>
  <limits max="100" default="100"/>
  <registration available="no" open="no"/>
  <searching>
    <search available="yes" supportedParams="q,group"/>
    <tv-search available="yes" supportedParams="q,rid,tvdbid,season,ep"/>
    <movie-search available="yes" supportedParams="q,imdbid,genre"/>
    <audio-search available="yes" supportedParams="q,album,artist,label,year,genre"/>
  </searching>
  <categories>
    <category id="1000" name="Console">
      <subcat id="1010" name="NDS" description="Console"/>
      <subcat id="1020" name="PSP" description="Console"/>
      <subcat id="1030" name="Wii" description="Console"/>
      <subcat id="1040" name="Xbox" description="Console"/>
      <subcat id="1050" name="Xbox 360" description="Console"/>
      <subcat id="1060" name="WiiWare/VC" description="Console"/>
      <subcat id="1070" name="XBOX 360 DLC" description="Console"/>
      <subcat id="1080" name="PS3" description="Console"/>
      <subcat id="1090" name="Xbox One" description="Console"/>
      <subcat id="1100" name="PS4" description="Console"/>
      <subcat id="1110" name="NSW" description="Console"/>
    </category>
    <category id="2000" name="Movies">
      <subcat id="2010" name="Foreign" description="Movies"/>
      <subcat id="2020" name="Other" description="Movies"/>
      <subcat id="2030" name="SD" description="Movies"/>
      <subcat id="2040" name="HD" description="Movies"/>
      <subcat id="2045" name="UHD" description="Movies"/>
      <subcat id="2050" name="BluRay" description="Movies"/>
      <subcat id="2060" name="3D" description="Movies"/>
    </category>
    <category id="3000" name="Audio">
      <subcat id="3010" name="MP3" description="Audio"/>
      <subcat id="3020" name="Video" description="Audio"/>
      <subcat id="3030" name="Audiobook" description="Audio"/>
      <subcat id="3040" name="Lossless" description="Audio"/>
    </category>
    <category id="4000" name="PC">
      <subcat id="4010" name="0day" description="PC"/>
      <subcat id="4020" name="ISO" description="PC"/>
      <subcat id="4030" name="Mac" description="PC"/>
      <subcat id="4040" name="Mobile-Other" description="PC"/>
      <subcat id="4050" name="Games" description="PC"/>
      <subcat id="4060" name="Mobile-iOS" description="PC"/>
      <subcat id="4070" name="Mobile-Android" description="PC"/>
    </category>
    <category id="5000" name="TV">
      <subcat id="5020" name="Foreign" description="TV"/>
      <subcat id="5030" name="SD" description="TV"/>
      <subcat id="5040" name="HD" description="TV"/>
      <subcat id="5045" name="UHD" description="TV"/>
      <subcat id="5050" name="Other" description="TV"/>
      <subcat id="5060" name="Sport" description="TV"/>
      <subcat id="5070" name="Anime" description="TV"/>
      <subcat id="5080" name="Documentary" description="TV"/>
    </category>
    <category id="6000" name="XXX">
      <subcat id="6010" name="DVD" description="XXX"/>
      <subcat id="6020" name="WMV" description="XXX"/>
      <subcat id="6030" name="XviD" description="XXX"/>
      <subcat id="6040" name="x264" description="XXX"/>
      <subcat id="6050" name="Pack" description="XXX"/>
      <subcat id="6060" name="ImgSet" description="XXX"/>
      <subcat id="6070" name="Other" description="XXX"/>
    </category>
    <category id="7000" name="Books">
      <subcat id="7010" name="Mags" description="Books"/>
      <subcat id="7020" name="Ebook" description="Books"/>
      <subcat id="7030" name="Comics" description="Books"/>
    </category>
    <category id="8000" name="Other">
      <subcat id="8010" name="Misc" description="Other"/>
    </category>
  </categories>
  <groups>
    <group id="104" name="alt.binaries.ebook" description="This group contains E-Books" lastupdate="Mon, 26 Sep 2016 17:23:28 +0000"/>
  </groups>
  <genres>
    <genre id="1" categoryid="3000" name="Hard Rock &amp; Metal"/>
    <genre id="999" categoryid="3000" name="Romanesque"/>
  </genres>
</caps>
"""
val GEN = """<?xml version="1.0"?>
<rss xmlns:atom="http://www.w3.org/2005/Atom" xmlns:newznab="http://www.newznab.com/DTD/2010/feeds/attributes/" version="2.0">
  <channel>
    <atom href="/binsearch?q=survivor+1080p+S05E02" rel="self" type="application/rss+xml"/>
    <title>binsearch.info</title>
    <description>binsearch.info API</description>
    <link>https://binsearch.info</link>
    <newznab:response offset="0" total="1"/>
    <item>
      <title>Saturday.Night.Live.S46E09.720p.HEVC.x265-MeGusta</title>
      <guid isPermaLink="false">918191431</guid>
      <pubDate>13-Nov-2020</pubDate>
      <enclosure url="https://api.nzbgeek.info/api?t=get&amp;id=9849875e4d3ab1f50e9088f03ec5b041&amp;apikey=b6a6285225034b82211e009cce008c8c" type="application/x-nzb"/>
    </item>
  </channel>
</rss>
"""

/*

    <item>
      <title>Primal.Survivor.S05E02.1080p.AMZN.WEB-DL.DD+5.1.H.264-Cinefeel</title>
      <guid isPermaLink="false">918191431</guid>
      <pubDate>13-Nov-2020</pubDate>
      <enclosure url="?id=918191431" type="application/x-nzb"/>
    </item>


 */
val SAMPLE = """<?xml version="1.0" encoding="utf-8"?>
<rss xmlns:atom="http://www.w3.org/2005/Atom" xmlns:newznab="http://www.newznab.com/DTD/2010/feeds/attributes/" version="2.0">
  <channel>
    <atom:link href="https://api.nzbgeek.info/api?t=tvsearch&amp;cat=5030,5040&amp;extended=1&amp;offset=0&amp;limit=100&amp;apikey=b6a6285225034b82211e009cce008c8c" rel="self" type="application/rss+xml"/>
    <title>api.nzbgeek.info</title>
    <description>NZBgeek API</description>
    <link>http://api.nzbgeek.info/</link>
    <language>en-gb</language>
    <webMaster>info@nzbgeek.info (NZBgeek)</webMaster>
    <category/>
    <image>
      <url>https://api.nzbgeek.info/covers/nzbgeek.png</url>
      <title>api.nzbgeek.info</title>
      <link>http://api.nzbgeek.info/</link>
      <description>NZBgeek</description>
    </image>
    <newznab:response offset="0" total="1"/>
    <item>
      <title>Cosmos.Possible.Worlds.S01E10.A.Tale.of.Two.Atoms.720p.HEVC.x265-MeGusta</title>
      <guid isPermaLink="true">https://api.nzbgeek.info/details/740b8bb23bd4d7c17a84e649be553095</guid>
      <link>https://api.nzbgeek.info/api?t=get&amp;id=740b8bb23bd4d7c17a84e649be553095&amp;apikey=b6a6285225034b82211e009cce008c8c</link>
      <comments>https://nzbgeek.info/geekseek.php?guid=740b8bb23bd4d7c17a84e649be553095</comments>
      <pubDate>Wed, 23 Dec 2020 04:13:18 +0000</pubDate>
      <category>TV &gt; HD</category>
      <description>Cosmos.Possible.Worlds.S01E10.A.Tale.of.Two.Atoms.720p.HEVC.x265-MeGusta</description>
      <enclosure url="https://api.nzbgeek.info/api?t=get&amp;id=740b8bb23bd4d7c17a84e649be553095&amp;apikey=b6a6285225034b82211e009cce008c8c" length="403157000" type="application/x-nzb"/>
      <newznab:attr name="category" value="5000"/>
      <newznab:attr name="category" value="5040"/>
      <newznab:attr name="size" value="403157000"/>
      <newznab:attr name="guid" value="740b8bb23bd4d7c17a84e649be553095"/>
      <newznab:attr name="tvdbid" value="260586"/>
      <newznab:attr name="season" value="S01"/>
      <newznab:attr name="episode" value="E10"/>
      <newznab:attr name="tvairdate" value="2014-05-11T00:00:00Z"/>
      <newznab:attr name="grabs" value="154"/>
      <newznab:attr name="usenetdate" value="Wed, 23 Dec 2020 04:10:42 +0000"/>
    </item>
      </channel>
</rss>
"""
