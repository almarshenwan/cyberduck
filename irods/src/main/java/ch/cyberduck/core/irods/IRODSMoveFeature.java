package ch.cyberduck.core.irods;

/*
 * Copyright (c) 2002-2015 David Kocher. All rights reserved.
 * http://cyberduck.ch/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * Bug fixes, suggestions and comments should be sent to feedback@cyberduck.ch
 */

import ch.cyberduck.core.Path;
import ch.cyberduck.core.exception.BackgroundException;
import ch.cyberduck.core.exception.NotfoundException;
import ch.cyberduck.core.features.Delete;
import ch.cyberduck.core.features.Move;

import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.pub.IRODSFileSystemAO;
import org.irods.jargon.core.pub.io.IRODSFile;

/**
 * @version $Id$
 */
public class IRODSMoveFeature implements Move {

    private IRODSSession session;

    public IRODSMoveFeature(IRODSSession session) {
        this.session = session;
    }

    @Override
    public void move(final Path file, final Path renamed, final boolean exists, final Delete.Callback callback) throws BackgroundException {
        try {
            final IRODSFileSystemAO fs = session.filesystem();
            final IRODSFile s = fs.getIRODSFileFactory().instanceIRODSFile(file.getAbsolute());
            if(!s.exists()) {
                throw new NotfoundException(String.format("%s doesn't exist", file.getAbsolute()));
            }
            final IRODSFile d = fs.getIRODSFileFactory().instanceIRODSFile(renamed.getAbsolute());
            s.renameTo(d);
        }
        catch(JargonException e) {
            throw new IRODSExceptionMappingService().map("Cannot rename {0}", e, file);
        }
    }

    @Override
    public boolean isSupported(final Path file) {
        return true;
    }
}