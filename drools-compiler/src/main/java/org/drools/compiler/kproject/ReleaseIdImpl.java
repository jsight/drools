package org.drools.compiler.kproject;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.StringReader;
import java.util.Properties;

import org.drools.core.util.StringUtils;
import org.kie.api.builder.ReleaseId;

public class ReleaseIdImpl implements ReleaseId, Externalizable {
    private String groupId;
    private String artifactId;
    private String version;

    private String snapshotVersion;

    public ReleaseIdImpl() { }

    public ReleaseIdImpl(String releaseId) {
        String[] split = releaseId.split(":");
        this.groupId = split[0];
        this.artifactId = split[1];
        this.version = split[2];
    }

    public ReleaseIdImpl(String groupId,
                         String artifactId,
                         String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }
    
    public String getGroupId() {
        return groupId;
    }
    
    public String getArtifactId() {
        return artifactId;
    }
    
    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return groupId + ":" + artifactId + ":" + version;
    }
    
    public String toExternalForm() {
        return toString();
    }
    
    public String getPomXmlPath() {
        return "META-INF/maven/" + groupId + "/" + artifactId + "/pom.xml";
    }

    public String getPomPropertiesPath() {
        return "META-INF/maven/" + groupId + "/" + artifactId + "/pom.properties";
    }   
    
    public String getCompilationCachePathPrefix() { 
        //return "META-INF/maven/" + groupId + "/" + artifactId + "/";
        return "META-INF/";
    }

    public boolean isSnapshot() {
        return version.endsWith("-SNAPSHOT");
    }
    
    public static ReleaseId fromPropertiesString(String string) {
        Properties props = new Properties();
        ReleaseId releaseId = null;
        try {
            props.load( new StringReader( string ) );
            String groupId = props.getProperty( "groupId" );
            String artifactId = props.getProperty( "artifactId" );
            String version = props.getProperty( "version" );            
            if ( StringUtils.isEmpty( groupId  ) || StringUtils.isEmpty( artifactId ) || StringUtils.isEmpty( version ) ) {
                throw new RuntimeException("pom.properties exists but ReleaseId content is malformed\n" + string);
            }
            releaseId =  new ReleaseIdImpl( groupId, artifactId, version );
        } catch ( IOException e ) {
            throw new RuntimeException( "pom.properties was malformed\n" + string, e );
        }
        
        return releaseId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReleaseIdImpl that = (ReleaseIdImpl) o;

        if (artifactId != null ? !artifactId.equals(that.artifactId) : that.artifactId != null) return false;
        if (groupId != null ? !groupId.equals(that.groupId) : that.groupId != null) return false;
        if (version != null ? !version.equals(that.version) : that.version != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = groupId != null ? groupId.hashCode() : 0;
        result = 31 * result + (artifactId != null ? artifactId.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }

    public String getSnapshotVersion() {
        return snapshotVersion;
    }

    public void setSnapshotVersion(String snapshotVersion) {
        this.snapshotVersion = snapshotVersion;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(groupId);
        out.writeObject(artifactId);
        out.writeObject(version);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        groupId = (String)in.readObject();
        artifactId = (String)in.readObject();
        version = (String)in.readObject();
    }
}
